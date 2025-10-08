<template>
  <div class="text-center">
    <h1 :style="{ color: 'white' }" :class="{isAddSimulation }">{{ title }}</h1>
    <div :class="[grid_size > 0 ? ['flex', 'flex-col', 'md:flex-row', 'justify-center', 'items-center'] : []]">
      <div class="flex flex-col">
        <Card class="card">
          <template #content>
            <div :class="['form-wrapper', 'max-w-96', 'max-md:mx-auto', 'max-md:w-5/6', grid_size > 0 ? 'md:mx-4' : 'md:mx-auto']">
              <div :class="[grid_size > 0 ? 'form-group-narrow' : 'form-group-wide']">
                <label for="averageIncome">Average Income:</label>
                <inputNumber v-if="isAddSimulation" type="number" id="averageIncome" :min="0"
                             placeholder="in CHF per month" :maxFractionDigits="10"
                             v-model.number="simulation.avgIncome.value"/>
                <inputNumber v-else type="number" id="averageIncome" :min="0" v-model.number="simulation.avgIncome.value"
                             placeholder="in CHF per month" :maxFractionDigits="10"/>
                <label for="commuterCost">Commuter Cost:</label>
                <inputNumber v-if="isAddSimulation" type="number" id="commuterCost" :min="0"
                             placeholder="in CHF/Km/month" :maxFractionDigits="10"
                             v-model.number="simulation.commuterCost.value"/>
                <inputNumber v-else type="number" id="commuterCost" :min="0"
                             v-model.number="simulation.commuterCost.value"
                             placeholder="in CHF/Km/month" :maxFractionDigits="10"/>
                <label v-if="!isAddSimulation" for="globalConstructionCostLimit" class="centered-label">Maximum Global <br> Construction Cost Limit:</label>
                <inputNumber v-if="!isAddSimulation" type="number"
                             id="globalConstructionCostLimit"
                             :min="0"
                             v-model.number="simulation.globalConstructionCostRestriction.value"
                             @focusout="checkForZero(true)"
                             placeholder="in CHF per Building" :maxFractionDigits="10"/>
                <label v-if="!isAddSimulation" for="globalRentCostLimit" class="centered-label">Maximum Global <br> Rent Cost Limit:</label>
                <inputNumber v-if="!isAddSimulation" type="number" id="globalRentCostLimit"
                             :min="0"
                             v-model.number="simulation.globalRentCostRestriction.value"
                             @focusout="checkForZero(true)"
                             placeholder="in CHF/km²" :maxFractionDigits="10"/>
                <label for="cityName">Simulation Name:</label>
                <inputText v-if="isAddSimulation" type="text" id="cityName"
                           v-model.trim="simulation.name"
                           class="city-name"
                           placeholder="Name of the simulation"/>
                <inputText v-else type="text" id="cityName" class="city-name"
                           v-model.trim="simulation.name"
                           placeholder="Name of the simulation"/>
              </div>
            </div>
          </template>
        </Card>
        <div v-if="!isAddSimulation && grid_size > 20" class="form-actions">
          <div :class="[!isAddSimulation ? ['flex', 'flex-col'] : 'save-button-group']">
            <button class="btn btn-save" @click="save(false, false)">Save</button>
            <button class="btn btn-save" @click="save(true, false)">Save & Compare</button>
          </div>
        </div>
      </div>
      <RestrictionsBuilder v-if="$route.href.endsWith('/edit')" :blocks="blocks" :grid_size="grid_size" />
    </div>
    <div v-if="isAddSimulation || grid_size <= 20" class="form-actions">
      <div class="save-button-group">
        <button class="btn btn-save" @click="save(false, false)">Save</button>
        <button v-if="isAddSimulation" class="btn btn-save" @click="restrictions">Add Restrictions</button>
        <button v-if="!isAddSimulation" class="btn btn-save" @click="save(true, false)">Save & Compare</button>
      </div>
    </div>
  </div>
  <div style="height: 220px"/> <!-- To push the footer down and leave room for the pop-up menu -->
</template>

<script>
  import axios from "../axios";
  import RestrictionsBuilder from "@/components/RestrictionsBuilder.vue";
  import {useToast} from "primevue/usetoast";
  import InputNumber from 'primevue/inputnumber';
  import Card from 'primevue/card';
  import InputText from 'primevue/inputtext';

  export default {
    components: {
      RestrictionsBuilder,
      InputNumber,
      Card,
      InputText
    },
    data() {
      return {
        simulation: {
          avgIncome: {value: null, currency: "CHF"},
          commuterCost: {value: null, currency: "CHF"},
          name: "",
          globalConstructionCostRestriction: {value: null, currency: 'CHF'},
          globalRentCostRestriction: {value: null, currency: 'CHF'}
        },
        id: null,
        blocks: new Map(),
        grid_size: 0,
        toast: useToast(),
        editingOldSim: false,

      };
    },
    watch: {
      '$route.path'(newPath) {
        if (newPath === '/add-simulation') {
          this.simulation.avgIncome.value = null;
          this.simulation.commuterCost.value = null;
          this.simulation.name = "";
        }
      }
    },
    methods: {
      checkForZero(flag) {
        let foundZero = false
        if (this.simulation.globalConstructionCostRestriction.value === 0 || this.simulation.globalRentCostRestriction.value === 0) {
          if (flag) {
            this.toast.add({
              severity: 'error',
              summary: 'Invalid value',
              detail: 'Giving 0 as a value is not allowed',
              life: 3000
            });
          }
        }
        if (this.simulation.globalConstructionCostRestriction.value === 0) {
          this.simulation.globalConstructionCostRestriction.value = null;
          foundZero = true
        }
        if (this.simulation.globalRentCostRestriction.value === 0) {
          this.simulation.globalRentCostRestriction.value = null;
          foundZero = true
        }
        return foundZero
      },
      isZoneRestricted(x, y) {
        return y < Math.abs(x) - (this.grid_size - 1) / 2 || y > -Math.abs(x) + (this.grid_size - 1) / 2;
      },
      validateForm() {
        const { name, avgIncome, commuterCost } = this.simulation;

        if (!name.trim()) {
          throw new Error("City name cannot be empty!");
        }

        if (!/^[A-Za-z0-9 ]+$/.test(name)) {
          throw new Error("City name must only contain letters or numbers!");
        }

        if (this.$route.path === '/add-simulation') {
          const data = {
            avgIncome: parseFloat(avgIncome.value),
            commuterCost: parseFloat(commuterCost.value),
            name: name.trim()
          };

          if (isNaN(data.avgIncome) || isNaN(data.commuterCost) || data.avgIncome <= 0 || data.commuterCost <= 0) {
            throw new Error("Invalid input");
          }
        } else {
          if (avgIncome.value <= 0 || commuterCost.value <= 0 || isNaN(avgIncome.value) || isNaN(commuterCost.value)) {
            throw new Error("Invalid input");
          }
        }

        return true;
      },
      validateBlocks() {
        let block;
        let coordinates;
        for (const blockKey of this.blocks.keys()) {
          coordinates = /^Coordinates\[x=([+-]?\d+), y=([+-]?\d+)\]$/.exec(
              blockKey
          );
          block = this.blocks.get(blockKey);
          if (
              !coordinates[1] || coordinates[1] < -(this.grid_size - 1) / 2 || coordinates[1] > (this.grid_size - 1) / 2 ||
              !coordinates[2] || coordinates[2] < -(this.grid_size - 1) / 2 || coordinates[2] > (this.grid_size - 1) / 2 ||
              !block ||
              (block.transportationCost && !isNaN(parseFloat(block.transportationCost)) && parseFloat(block.transportationCost)) < 0 ||
              (block.constructionCost && block.blockType !== "RESIDENTIALBLOCK") ||
              (block.constructionCost && !isNaN(parseFloat(block.constructionCost)) && parseFloat(block.constructionCost)) < 0 ||
              (block.rentPrice && block.blockType !== "RESIDENTIALBLOCK") ||
              (block.rentPrice && !isNaN(parseFloat(block.rentPrice)) && parseFloat(block.rentPrice)) < 0 ||
              (block.restriction &&
                  block.restriction !== "ANY"
                  && block.restriction !== "RESIDENTIALBLOCK"
                  && block.restriction !== "PARKBLOCK"
                  && block.restriction !== "WATERBLOCK"
              ) // UNCOMMENT WHEN OUTSIDE OF THE CITY THERE WON'T BE RESIDENTIALBLOCK
                // || this.isZoneRestricted(coordinates[1], coordinates[2]) && block.blockType == "RESIDENTIALBLOCK"
          ) {
            console.error(
                `Error was caused by {${blockKey} : ${JSON.stringify(block)}}`
            );
            throw new Error(`Error parsing block with a key '${blockKey}'`);
          }
        }
      },
      async save(compare, redirect) {
        try {
          this.validateForm();
          this.validateBlocks();
        } catch (error) {
          this.toast.add({
            severity: "error",
            summary: error.name,
            position: "top-center",
            detail: error.message,
            life: 2000
          });
          return;
        }

        let content = "saving details";
        let timer = 1000;

        if (this.$route.path === '/add-simulation') {
          // recreate data which you are sure it exist now

          const data = {
            avgIncome: this.simulation.avgIncome,
            commuterCost: this.simulation.commuterCost,
            name: this.simulation.name.trim(),
            globalConstructionCostRestriction: {value: 0, currency: 'CHF'},
            globalRentCostRestriction: {value: 0, currency: 'CHF'}
          };
          try {
            let res;
            res = await axios.post("/simulations/new", data);
            this.id = res.data.id;
            if (res && res.status === 206) {
              content = "The simulation has a radius greater than 20 blocks, so it has been limited";
              timer = 3000;
            }

          } catch (error) {
            console.error(error.message);
            if (error.response.status === 400) {
              this.toast.add({
                severity: "error",
                summary: "Parameters received are not valid",
                position: "top-center",
                detail: "Parameters received are not valid",
                life: 2000
              });
            } else {
              this.toast.add({
                severity: "error",
                summary: "Unknown request error",
                position: "top-center",
                detail: "Unknown request error",
                life: 2000
              });
            }
            return
          }
        } else {
          this.simulation.name = this.simulation.name.trim();
          this.simulation.restrictionsJSON = Array.from(this.blocks, ([coordinates, value]) => ({
            coordinates,
            blockType: value.blockType,
            blockTypeRestriction: value.blockTypeRestriction,
            localLimitRentCost: value.localLimitRentCost,
            localLimitConstructionCost: value.localLimitConstructionCost,
            localLimitTransportationCost: value.localLimitTransportationCost
          }));

          this.simulation.restrictionsJSON = JSON.stringify(this.simulation.restrictionsJSON);

          if (this.simulation.globalConstructionCostRestriction.value == null) {
            this.simulation.globalConstructionCostRestriction.value=0
          }

          if (this.simulation.globalRentCostRestriction.value == null) {
            this.simulation.globalRentCostRestriction.value=0
          }

          try {
            let response;
            if (!this.editingOldSim) {
              response = await axios.put("/simulations/" + this.$route.params.id + "/edit?compare=" + compare + "&editOld=" + this.editingOldSim, this.simulation);
            } else if (this.editingOldSim) {
              response = await axios.put("/simulations/" + this.$route.params.id + "/edit?compare=" + compare + "&editOld=" + this.editingOldSim, this.simulation);
            }

            console.log(response.status)
            if (response && response.status === 206) {
              content = "The simulation has been created successfully but has a radius greater than 20 blocks, so it has been limited";
              timer = 3000;
            }
          } catch (error) {
            console.error(error.message);
            if (error.response.status === 404) {
              this.toast.add({
                severity: "error",
                summary: "Simulation not found",
                position: "top-center",
                detail: "Simulation not found",
                life: 2000
              });
            } else if (error.response.status === 400) {
                this.toast.add({
                  severity: "error",
                  summary: "Parameters received are not valid",
                  position: "top-center",
                  detail: "Parameters received are not valid",
                  life: 2000
                });
            } else {
              this.toast.add({
                severity: "error",
                summary: "Unknown request error",
                position: "top-center",
                detail: "Unknown request error",
                life: 2000
              });
            }
            return;
          }
        }

        this.toast.add({
          severity: "success",
          summary: content,
          position: "top-center",
          detail: "Details saved successfully",
          life: timer
        });

        if (!redirect) {
          setTimeout(() => {
            this.$router.push("/simulations/mine");
          }, timer);
        }
      },
      async restrictions() {
        try {
          await this.save(false, true);
        } catch (error) {
          console.error("Error while redirecting", error);
          return
        }
        if (this.id) {
          this.toast.add({
            severity: "info",
            position: "top-center",
            detail: "Navigating to add restrictions",
            life: 2000,
          });
          setTimeout(() => {
            window.location.href = "/simulations/" + this.id + "/edit"
          }, 2000);
        }
      },

    },
    async mounted() {
      if (this.$route.path !== '/add-simulation' && this.$route.path.endsWith("/edit")) {
        try {
          // prevent edge cases
          this.editingOldSim = this.$store.state.editingOldSimulation;
          this.$store.state.editingOldSimulation = false;

          const response = await axios.get("/simulations/" + this.$route.params.id)
          let responseData = response.data;

          if (this.editingOldSim) {
            const responseOld = await axios.get(`/simulations/${responseData.oldSimulation.id}`);
            responseData = responseOld.data;
          }

          this.blocks = new Map(Object.entries(responseData.city.blocks));
          this.grid_size = responseData.city.size;

          //oldversion edit activated
          if (this.editingOldSim) {

            //create the old version simulation
            this.simulation = {
              avgIncome: { value: responseData.averageIncome.value, currency: responseData.averageIncome.currency },
              commuterCost: { value: responseData.commuterCosts.value, currency: responseData.commuterCosts.currency },
              name: responseData.name,
              globalConstructionCostRestriction: { value: responseData.city.globalMaxConstructionCost.value, currency: responseData.city.globalMaxConstructionCost.currency },
              globalRentCostRestriction: { value: responseData.city.globalMaxRentCost.value, currency: responseData.city.globalMaxRentCost.currency }
            }
          } else {
            //create the simulation normally
            this.simulation = {
              avgIncome: { value: responseData.averageIncome.value, currency: responseData.averageIncome.currency },
              commuterCost: { value: responseData.commuterCosts.value, currency: responseData.commuterCosts.currency },
              name: responseData.name,
              globalConstructionCostRestriction: { value: responseData.city.globalMaxConstructionCost.value, currency: responseData.city.globalMaxConstructionCost.currency },
              globalRentCostRestriction: { value: responseData.city.globalMaxRentCost.value, currency: responseData.city.globalMaxRentCost.currency }
            }
          }
        } catch (error) {
          if (error.response && error.response.status === 404) {
            this.$router.push("/NotFound");
            this.toast.add({
              severity: "error",
              summary: "Simulation not found",
              position: "top-center",
              detail: "Simulation not found",
              life: 2000
            });
          } else if (error.response && error.response.status === 400) {
            this.toast.add({
              severity: "error",
              summary: "Invalid request",
              position: "top-center",
              detail: "Invalid request",
              life: 2000
            });
          } else {
            console.log(error);
            this.toast.add({
              severity: "error",
              summary: "Unknown error",
              position: "top-center",
              detail: "Unknown simulation creation error",
              life: 2000
            });
          }
        }
        this.checkForZero(false)
      }
    },
    computed: {
      isAddSimulation() {
        return this.$route.path === '/add-simulation';
      },
      title() {
        if (this.$route.path === '/add-simulation') return "Create A New Simulation";
        if (this.grid_size < 20) return `Modifying ${this.simulation.name}`;
        if (this.$route.path !== '/add-simulation' && this.grid_size > 20) return `Modifying ${this.simulation.name}`;
        return "";
      },
      globalConstructionCost: {
        get() {
          return this.globalConstructionCostRestriction ? this.block.globalConstructionCostRestriction.value : this.block.localLimitTransportationCost;
        },
        set(value) {
          if (this.block.localLimitTransportationCost) {
            this.block.localLimitTransportationCost.value = value;
          }
        }
      },
      globalRentCost: {
        get() {
          return this.block.localLimitConstructionCost ? this.block.localLimitConstructionCost.value : this.block.localLimitConstructionCost;
        },
        set(value) {
          if (this.block.localLimitConstructionCost) {
            this.block.localLimitConstructionCost.value = value;
          }
        }
      }
    }
  };
  </script>

  <style scoped>
  h1 {
    color: Black;
    padding: 1rem 0 1rem 0;
    font-size: x-large;
    font-weight: bold;
  }


  .form-wrapper {
    display: flex;
    background-color: #f4f4f4;
    padding: min(3vw, 1rem);
    border-radius: 10px;
    align-items: center;
    justify-content: center;
  }

  .form-group-narrow {
    display: flex;
    flex-direction: column;
  }

  .form-group-wide {
    display: grid;
    grid-template-columns: 1fr 3fr;
    align-items: center;
  }

  .form-group-narrow label {
    text-align: center;
    margin: 1rem 0.25rem 0.5rem 0.25rem;
  }

  .form-group-narrow label:first-child {
    margin-top: 0;
  }

  .form-group-wide label {
    text-align: left;
    padding: 15% 0.25rem;
  }

  .form-group-wide input {
    padding: 0.75rem;
    border: 1px solid #ccc;
    border-radius: 0.5rem;
  }

  .form-group-narrow input {
    width: 90%;
    margin: 0.2rem 5%;
    padding: 0.5rem;
    border: 1px solid #ccc;
    border-radius: 0.5rem;
  }

  label {
    font-weight: bold;
  }

  .form-actions {
    display: flex;
    justify-content: center;
    margin-top: 1rem;
  }

  .save-button-group {
    display: flex;
    gap: 1rem;
  }

  .btn {
    margin-top: 1rem;
    padding: 0.75rem 1.5rem;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    font-weight: bold;
    text-transform: uppercase;
  }

  .save-button-group .btn {
    margin-right: 20px;
  }

  .save-button-group .btn:last-child {
    margin-right: 0;
  }

  .btn-save {
    background-color: #4caf50;
    color: white;
    user-select: none;
  }

  .centered-label {
    display: block;
    text-align: center;
    margin-top: 1rem;
  }

  .card {
    background: none;
    box-shadow: none;
  }

  .city-name:hover {
    border-color: rgb(var(--primary-400)) !important;
  }

  @media (prefers-color-scheme: dark) {
    .form-wrapper {
      background-color: rgb(40, 60, 90);
      display: flex;
      padding: min(3vw, 1rem);
      border-radius: 10px;
      align-items: center;
      justify-content: center;
    }

    .city-name {
      border: 1px solid rgb(71, 85, 105) !important;
      box-shadow: none;
      background-color: rgb(15, 23, 42);
    }
  }
  </style>

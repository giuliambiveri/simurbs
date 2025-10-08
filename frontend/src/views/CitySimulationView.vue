<script>
import renderCity from '@/components/RenderCity.vue';
import CompareInfoBox from '@/components/CompareInfoBox.vue';
import axios from "../axios";
import {Icon} from "@iconify/vue";
import {useToast} from "primevue/usetoast";
import Button from 'primevue/button';

export default {
  name: "CitySimulationView",
  components: {
    renderCity,
    Icon,
    Button,
    CompareInfoBox,
  },
  computed: {
    renderCityKey() {
      return this.isSplitView ? 'split' : 'single';
    }
  },
  data() {
    const simulationInfo = () => ({
      averageIncome: 0,
      commuterCosts: 0,
      populationSize: 0,
      name: "",
      Simulation: null,
    });

    const currentSimulationInfo = () => ({
      ...simulationInfo(),
      cityId: null,
      isMine: null,
    });

    const cityInfo = () => ({
      id: null,
      population: 0,
      radius: 0,
      size: 0,
      blockCounter: 0,
      globalMaxConstructionCost: 0,
      globalMaxRentCost: 0,
      blocks: null
    });

    return {
      isSplitView: this.$store.state.isCompareMode,
      simulationInfo: currentSimulationInfo(),
      oldSimulationInfo: simulationInfo(),
      city: cityInfo(),
      oldCity: cityInfo(),
      isSimulationPublic: false,
      isPublicRoute: this.$route.name === 'simulationPublicView',
      mapFetched: true,
      toast: useToast(),
      isComparePopupOpen: false,
      currentBlockData: {coordinates: {x: null, z: null}},
      compareBlockData: null
    }
  },
  methods: {
    toggleSimulationPublic() {
      this.isSimulationPublic = !this.isSimulationPublic;
      try {
        axios.patch("/simulations/" + this.$route.params.id + "/togglePrivacy")
      } catch (error) {
        let errorMessage = "Unknown error";
        let errorDetail = "Unknown simulation request error";

        if (error.response) {
          if (error.response.status === 404) {
            errorMessage = "Simulation not found";
            errorDetail = "Simulation not found";
          } else if (error.response.status === 400) {
            errorMessage = "Invalid request";
            errorDetail = "Invalid request";
          }
        }

        console.error(error);
        this.toast.add({
          severity: "error",
          summary: errorMessage,
          position: "top-center",
          detail: errorDetail,
          life: 2000
        });
      }
    },
    copySimulationLink() {
      const simulationPublicLink = `${window.location.origin}/simulations/public/${this.$route.params.id}`;
      navigator.clipboard.writeText(simulationPublicLink)
          .then(() => {
            this.toast.add({
              severity: "info",
              position: "top-center",
              detail: "Link copied to clipboard!",
              life: 2000,
            });
          })
          .catch(err => {
            console.error('Error copying link to clipboard', err);
            this.toast.add({
              severity: "error",
              position: "top-center",
              detail: "Error copying link to clipboard",
              life: 2000
            });
          });
    },
    toggleView() {
      if (this.oldCity.id == null) {
        this.toast.add({
          severity: "error",
          position: "top-center",
          detail: "No comparison mode available.",
          life: 2000
        });
      } else {
        this.$store.state.isCompareMode = !this.$store.state.isCompareMode;
        this.isSplitView = this.$store.state.isCompareMode;
      }
    },
    checkIfPublicRoute() {
      return this.$route.path.includes("/public/");
    },
    handleOldEdit() {
      this.$store.state.editingOldSimulation = true;
      this.$router.push('/simulations/' + this.$route.params.id + '/edit')
    },
    handleExit() {
      this.$router.push('/simulations/mine');
    },
    showComparePopup(currentBlock, compareBlock) {
      const defaultCost = {
        value: 0,
        currency: 'CHF'
      };
      const nonResidentialExtension = {
        building: {
          height: 0,
          people: 0
        },
        constructionCost: defaultCost,
        localLimitConstructionCost: defaultCost,
        rentCost: defaultCost,
        localLimitRentCost: defaultCost,
        transportationCost: defaultCost,
        localLimitTransportationCost: defaultCost,
        landPrice: defaultCost
      }

      const setData = (block, data) => {
        if (block === null) {
          return null;
        } else {
          return block.blockType === 'RESIDENTIALBLOCK' ? block : {...block, ...data};
        }
      };

      this.currentBlockData = setData(currentBlock, nonResidentialExtension);
      this.compareBlockData = setData(compareBlock, nonResidentialExtension);

      console.log('here i set blocks to be compared', this.currentBlockData, this.compareBlockData)
      this.isComparePopupOpen = true;
    },
    closeComparePopup() {
      this.isComparePopupOpen = false;
    },
    updateBlockInfo(newBlockInfo) {
      this.currentBlockData = newBlockInfo;
    },
    updateComparisonBlockInfo(newComparisonBlockInfo) {
      this.compareBlockData = newComparisonBlockInfo;
    },
    hasOldCity() {
      return this.oldCity && this.oldCity.id;
    }

  },
  handleErrorResponse(errorMessage) {
    const redir = "Redirecting to simulations list";
    this.toast.add({
      severity: "error",
      summary: errorMessage,
      position: "top-center",
      detail: redir,
      life: 5000
    });
  },
  async mounted() {
    try {

      // do this to avoid remaining of split view
      this.$store.state.isCompareMode = false;
      this.isSplitView = this.$store.state.isCompareMode;

      // make sure that this variable is always set to false at creation
      this.$store.state.editingOldSimulation = false;

      let response;
      const simulationId = this.$route.params.id;

      if (this.isPublicRoute) {
        response = await axios.get(`/simulations/public/${simulationId}`);
      } else {
        response = await axios.get(`/simulations/${simulationId}`);
      }

      if (response.data.isPublic) {
        this.isSimulationPublic = true
      }

      // current not old city
      const cityData = response.data.city;
      this.city = cityData;
      console.log(cityData);
      this.simulationInfo = {
        ...response.data,
        city: cityData.id,
        isMine: !this.checkIfPublicRoute()
      };

      // old city
      if (response.data.oldSimulation != null) {
        const oldCityData = response.data.oldSimulation.simulatedCity;
        this.oldCity = oldCityData;
        this.oldSimulationInfo = {...response.data.oldSimulation}
      }
    } catch (error) {
      this.mapFetched = false
      if (error.response) {
        switch (error.response.status) {
          case 404:
            this.handleErrorResponse("Simulation not found");
            break;
          case 400:
            this.handleErrorResponse("Invalid request");
            break;
          default:
            console.error(error);
            this.handleErrorResponse("Unknown request error");
            break;
        }
        setTimeout(() => {
          this.$router.push("/simulations/mine");
        }, 5000);
      } else if (error.request) {
        // Handle no response from server
        console.error("No response from server. Check your network connection.");
        this.toast.add({
          severity: "error",
          summary: "No response",
          position: "top-center",
          detail: "No response from server. Check your network connection.",
          life: 2000
        });
      } else {
        // Handle other errors
        console.error("Error:", error.message);
        this.toast.add({
          severity: "error",
          summary: "Unexpected error",
          position: "top-center",
          detail: "An unexpected error occurred.",
          life: 2000
        });
      }
    }

  }
}

</script>

<template>
  <div style="min-height:100%;min-width:100%">
    <div class="simulation-container font-bold">
      <Button icon="pi pi-arrow-left" @click="handleExit()" :class="{ 'arrow-button1': !simulationInfo.isMine, 'arrow-button': simulationInfo.isMine }"></Button>
      <div>
        <div v-if="simulationInfo.isMine">
          <div v-if="isSplitView" class="button-container1">
            <Button @click="handleOldEdit" v-if="isSplitView" class="edit-btn mr-2">
              Edit Old
              <Icon icon="material-symbols:edit-outline-rounded"/>
            </Button>
            <Button @click="toggleView" v-if="simulationInfo.oldSimulation" class="mr-2">
              {{ isSplitView ? 'Single View' : 'Comparison' }}
              <Icon :icon="isSplitView ? 'material-symbols:image-outline-rounded' : 'material-symbols:compare-rounded'"/>
            </Button>
            <Button @click="$router.push('/simulations/' + $route.params.id + '/edit')" v-if="isSplitView" class="">
              Edit New
              <Icon icon="material-symbols:edit-outline-rounded"/>
            </Button>
          </div>
          <div v-else class="button-container">
            <Button @click="toggleView" v-if="simulationInfo.oldSimulation" class="mr-2">
              {{ isSplitView ? 'Single View' : 'Comparison' }}
              <Icon :icon="isSplitView ? 'material-symbols:image-outline-rounded' : 'material-symbols:compare-rounded'"/>
            </Button>
            <Button @click="toggleSimulationPublic" class="mr-2">
              {{ isSimulationPublic ? ' Make Private' : 'Make Public' }}
              <Icon :icon="isSimulationPublic ? 'material-symbols:lock-outline' : 'material-symbols:lock-open-outline'"/>
            </Button>
            <Button @click="copySimulationLink" v-if="!isSplitView && isSimulationPublic" class="mr-2">
              Copy Link
              <Icon icon="material-symbols:content-copy-outline"/>
            </Button>
            <Button @click="$router.push('/simulations/' + $route.params.id + '/edit')" class="">
              Edit
              <Icon icon="material-symbols:edit-outline-rounded"/>
            </Button>
          </div>
        </div>
      </div>

      <div class="flex">
        <div class="flex-1" v-if="isSplitView">
          <renderCity
              :key="renderCityKey"
              :city="oldCity"
              :blocksToCompare="city.blocks"
              :simulationInfo="oldSimulationInfo"
              :isSplitView="isSplitView"
              :isOld="true"
              :isSimulationPublic="isSimulationPublic"
              @show-comparison="showComparePopup"
              @update-block-info="updateBlockInfo"
              @update-comparison-block-info="updateComparisonBlockInfo"
              @close-comparison="closeComparePopup"/>
        </div>
        <div v-if="isSplitView" class="bg-surface-200 dark:bg-surface-700 w-3 z-10"> </div>
        <div class="flex-1 h-screen" v-if="this.city.id != null">
          <div class="flex-1 h-screen" v-if="isSplitView || hasOldCity || !hasOldCity">
            <renderCity
                :key="renderCityKey"
                :city="city"
                :blocksToCompare="hasOldCity ? oldCity.blocks : null"
                :simulationInfo="simulationInfo"
                :isSplitView="isSplitView"
                :isOld="false"
                :isSimulationPublic="isSimulationPublic"
                @show-comparison="showComparePopup"
                @update-block-info="updateBlockInfo"
                @update-comparison-block-info="updateComparisonBlockInfo"
                @close-comparison="closeComparePopup"/>
          </div>
        </div>
      </div>
    </div>
    <CompareInfoBox
        :isOpen="isComparePopupOpen"
        :blockInfo="currentBlockData"
        :comparisonBlockInfo="compareBlockData"
        @close="closeComparePopup"
    />
  </div>
</template>


<style scoped>
.simulation-container {
  position: relative;
}

.button-container {
  position: absolute;
  top: 19px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 10px;
  z-index: 1000;
}

.button-container1 {
  position: absolute;
  bottom: 19px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 10px;
  z-index: 1000;
}

Button {
  border: none;
  cursor: pointer;
  font-size: 16px;
}

.flex-1 {
  position: relative;
}

.arrow-button {
  position: absolute;
  left: 1%;
  top: 19px;
  z-index: 1000;
  width: 50px;
}

.arrow-button1 {
  position: absolute;
  top: 19px;
  left: 3%;
  transform: translateX(-90%);
  z-index: 1000;
}

</style>

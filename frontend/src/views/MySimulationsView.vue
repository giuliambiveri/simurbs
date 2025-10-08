<script>
import Button from "primevue/button";
import SplitButton from 'primevue/splitbutton';
import {useConfirm} from "primevue/useconfirm";
import {useToast} from "primevue/usetoast";
import axios from "../axios";
import {Icon} from "@iconify/vue";

export default {
  name: "MySimulationsView",
  components: {
    Icon,
    Button,
    SplitButton
  },
  data() {
    return {
      simulations: [],
      sort: null,
      confirm: useConfirm(),
      toast: useToast()
    };
  },

  async mounted() {
    this.sort=0;
    const response = await axios.get("/simulations/mine");
    this.simulations=response.data
    this.simulations.sort(this.compare);
  },
  methods: {
    clickSort(A){
      if((this.sort-A===1||this.sort-A===0)&&this.sort%2===0) this.sort++;
      else this.sort=A;
      this.simulations.sort(this.compare)
    },
    compareAUX(A, B, prop, I) {
      let propA = A[prop], propB = B[prop];
      if (typeof propA === "string" && typeof propB === "string") {
        propA = propA.toLowerCase();
        propB = propB.toLowerCase();
      }
      if (I === 0) return this.compareLettersAndNumbers(propA, propB);
      return this.compareLettersAndNumbers(propB, propA);
    },
    compareLettersAndNumbers(a, b) {
      const ax = [], bx = [];
      a.toString().replace(/(\d+)|(\D+)/g, (_, $1, $2) => {
        ax.push([$1 || Infinity, $2 || ""])
      });
      b.toString().replace(/(\d+)|(\D+)/g, (_, $1, $2) => {
        bx.push([$1 || Infinity, $2 || ""])
      });
      while (ax.length && bx.length) {
        const an = ax.shift();
        const bn = bx.shift();
        const nn = (an[0] - bn[0]) || an[1].localeCompare(bn[1]);
        if (nn) return nn;
      }
      return ax.length - bx.length;
    },
    compare(A,B){
      let val=1
      let name;
      if(this.sort%2===0) val=0;
      if(this.sort<2) name="name";
      else if(this.sort<4) name="creationDate";
      else if(this.sort<6) name="views";
      else if(this.sort<8) name="population";

      return this.compareAUX(A,B,name,val);
    },
    edit(product) {
      this.$router.push("/simulations/" + product.id + "/edit")
    },
    async deleteSimulation(simulation,i) {
      try {
        await axios.delete("/simulations/" + simulation.id);
        let first=this.simulations.slice(0,i)
        let second=this.simulations.slice(i+1,this.simulations.length)
        this.simulations=first.concat(second)
        this.toast.add({
          severity: 'success',
          summary: 'Deletion completed',
          position: "top-center",
          detail: 'Simulation deleted successfully',
          life: 3000
        });
      } catch (e) {
        this.toast.add({severity: 'error', summary: e.name, position: "top-center", detail: e.message, life: 2000});

      }

    },
    async lock(product,i) {
      await axios.patch("/simulations/" + product.id + "/togglePrivacy",)
      this.simulations[i].isPublic=!this.simulations[i].isPublic;
    },
    view(product) {
      this.$router.push("/simulations/" + product.id);
    },
    async copy(item) {
      const simulationPublicLink = `${window.location.origin}/simulations/public/` + item.id;
      navigator.clipboard.writeText(simulationPublicLink)
          .then(() => {
            this.toast.add({severity: 'success', summary: "Link copied", position: "top-center", detail: "The link has been copied successfully", life: 2000});
          })
          .catch(err => {
            this.toast.add({severity: 'error', summary: err.name, position: "top-center", detail: e.message, life: 2000});
          });

    },
    confirmDelete(simulation,i) {
      this.confirm.require({
        message: 'Do you want to delete this simulation?',
        header: 'Delete Confirmation',
        accept: () => {
          this.deleteSimulation(simulation,i)
        },
        reject: () => {
          this.toast.add({severity: 'warn', summary: "Deletion canceled", position: "top-center", detail:"The delete has been canceled, nothing changed", life: 2000});
        }
      });
    },
    getCopyItem(product) {
      return [
        {
          label: 'Copy Link',
          icon: 'pi pi-copy',
          command: () => {
            this.copy(product);
          }
        }
      ];
    }
  }
}

</script>

<template>
  <div class="card">
    <table class="w-full bg-gray-500 text-white">
      <thead>
      <tr class="text-surface-700 dark:text-surface-0/80 bg-surface-300 dark:bg-surface-900" style="font-size:18px">
        <th style="width: 20%;" class="rounded-l-lg" @click=clickSort(0)>NAME <i class="pi" :class="{'pi-sort-amount-up': this.sort === 0, 'pi-sort-amount-down': this.sort === 1}"></i></th>
        <th style="width: 20%;" @click=clickSort(2)>DATE OF CREATION<i class="pi" :class="{'pi-sort-amount-up': this.sort === 2, 'pi-sort-amount-down': this.sort === 3}"></i></th>
        <th style="width: 10%;" @click=clickSort(4)>VIEWS<i class="pi" :class="{'pi-sort-amount-up': this.sort === 4, 'pi-sort-amount-down': this.sort === 5}"></i></th>
        <th style="width: 10%;" @click=clickSort(6)>POPULATION<i class="pi" :class="{'pi-sort-amount-up': this.sort === 6, 'pi-sort-amount-down': this.sort === 7}"></i></th>
        <th style="width: 10%;">COMPARISON MODE</th>
        <th style="width: 30%;" class="rounded-r-lg">ACTIONS</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(simulation,index) in simulations" style="font-size:18px" class="text-surface-700 dark:text-surface-0/80 bg-surface-100 dark:bg-surface-700">
        <td>
          {{ simulation.name }}
        </td>
        <td>
          {{ new Date(simulation.creationDate * 1000).toLocaleDateString() }}
          {{ new Date(simulation.creationDate * 1000).toLocaleTimeString() }}
        </td>
        <td>
          {{ simulation.isPublic ? simulation.views : "Not Public" }}
        </td>
        <td>
          {{ simulation.population.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") }}
        </td>
        <td>
          {{ simulation.oldSimulation?"Available":"Not Available"}}
        </td>
        <td>
          <div class="grid grid-cols-2 xl:grid-cols-4 gap-1">
            <Button icon="pi pi-eye" @click=view(simulation) label="View"></Button>
            <Button icon="pi pi-pencil" @click=edit(simulation) label="Edit"></Button>
            <SplitButton v-if="simulation.isPublic" :model="getCopyItem(simulation)" label="Lock" @click="lock(simulation,index)">
              <span class="flex justify-between font-bold w-full">
<!--                <span class="pi pi-lock-open"></span>-->
                <span>Lock</span>
              </span>
            </SplitButton>
            <Button v-else icon="pi pi-lock" @click=lock(simulation,index) label="Unlock"></Button>
            <Button @click="confirmDelete(simulation,index)" icon="pi pi-times" label="Delete" severity="danger"></Button>
          </div>
        </td>

      </tr>
      </tbody>
    </table>
  </div>
</template>

<style scoped>
th, td {
  padding-top: 10px;
  padding-bottom: 10px;
  border: 2px solid rgb(var(--surface-600));
  text-align: center;
}

th {
  cursor: pointer;
}

tr:first-child td:first-child {
  border-top-left-radius: 10px;
}

tr:first-child td:last-child {
  border-top-right-radius: 10px;
}

tr:last-child td:first-child {
  border-bottom-left-radius: 10px;
}

tr:last-child td:last-child {
  border-bottom-right-radius: 10px;
}

@media (prefers-color-scheme: dark) {
  th, td {
    padding-top: 10px;
    padding-bottom: 10px;
    border: 2px solid rgb(var(--surface-500));
  }
}
th{
  cursor: pointer;
}

.card {
  overflow-x: auto;
}

table {
  width: 100%;
  min-width: 600px;
}

@media (max-width: 768px) {
  th, td {
    font-size: 14px;
    padding: 5px;
  }

  .actions {
    flex-direction: column;
  }

  .actions button {
    margin-bottom: 5px;
  }
}
</style>
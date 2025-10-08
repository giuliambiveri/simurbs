<template>
  <div>
    <div class="justify-center items-center dark:text-primary-50" style="z-index: 999;" v-if="!isLoading && user">
      <div class="text-center m-10">
        <Avatar :label="userInitials" class="mr-2 font-bold" style="background-color: #dee9fc; color: #1a2551" size="xlarge" shape="circle" />
        <h2 class="text-center text-2xl font-bold mt-3">{{ user.firstName }} {{ user.lastName }}</h2>
        <p class="text-center dark:text-primary-50 text-gray-600 mt-1">{{ user.email }}</p>
      </div>
      <div class="box-border h-50 mx-auto flex flex-col items-center ">
        <template v-for="(panel,index) in simulationPanels">
          <Panel :toggleable="panel.toggleable && !isCurrentUser" :collapsed="true" class="w-full md:w-2/3 lg:w-1/3 mb-4" >
            <template #togglericon>
              <span class="p-2.5"  :class="panelsCollapsed[index] ? 'pi pi-chevron-down' : 'pi pi-chevron-up' "  @click="toggleButton(index)"></span>
            </template>
            <template #header>
              <div class="flex">
                <strong>{{ panel.header }}</strong>
                <p class="ml-2">{{ formatNumber(panel.value) }}</p>
              </div>
            </template>
            <template #icons>
              <Menu ref="menu" id="config_menu" popup />
            </template>
            <div class="text-left mb-2">
              <strong>{{ panel.subHeader }}</strong>
            </div>
            <ul class="text-left">
              <li v-for="(simulation, index) in simulations" :key="index" class="flex items-center">
                <i class="pi pi-circle-fill mr-2" style="font-size: 0.4rem"></i>
                <template v-if="panel.header !== 'Simulations:'">
                  {{ simulation.name }} :
                </template>
                {{ panel.header === 'Simulations:' ? simulation.name : panel.formattedField ? panel.formattedField(simulation) : formatNumber(simulation[panel.dataField]) }}
              </li>
            </ul>
          </Panel>
        </template>
      </div>
    </div>
    <div v-else-if="isLoading">
      <p class="text-lg font-semibold text-gray-700 dark:text-primary-50">Please wait, the page is being loaded...</p>
    </div>
    <div v-else>
      <NotFound/>
    </div>
  </div>
</template>

<script>
import { Icon } from "@iconify/vue";
import axios from "../axios";
import NotFound from "./NotFound.vue";
import {useToast} from "primevue/usetoast";
import Avatar from "primevue/avatar";
import Panel from "primevue/panel";
import Menu from "primevue/menu";

export default {
  name: "UserProfileView",
  components: {
    Icon,
    NotFound,
    Avatar,
    Panel,
    Menu
  },
  data() {
    return {
      UserId : null,
      user: null,
      numberOfSimulations: 0,
      averagePopulationSize: 0,
      averageIncome: 0,
      averageCommuterCosts: 0,
      isLoading: true,
      userFirstName: '',
      userLastName: '',
      simulations: [],
      panelsCollapsed: [],
      toast: useToast(),
    }
  },
  async mounted() {
    this.panelsCollapsed = Array(this.simulationPanels.length).fill(true);
    await this.loadUserProfile();
  },
  watch: {
    '$route'() {
      this.loadUserProfile();
    }
  },
  methods: {
    async loadUserProfile() {
      try {
        let userId;
        if (this.$route.name === 'userprofile') {
          const response = await axios.get(`/users/profile`);
          this.user = response.data;
          userId = this.user.id;
        } else {
          userId = this.$route.params.id;
          const response = await axios.get(`/users/${userId}/profile`);
          this.user = response.data;
        }
        this.UserId = userId;
        this.numberOfSimulations = this.user.count;
        this.averagePopulationSize = this.user.averagePopulation;
        this.averageIncome = this.user.averageIncome;
        this.averageCommuterCosts = this.user.averageCommuter;
        this.userFirstName = this.user.firstName;
        this.userLastName = this.user.lastName;

        await this.fetchSimulations(userId);

      } catch (error) {
        console.error("Error retrieving user profile:", error);
        this.toast.add({
          severity: "error",
          position: "top-center",
          detail: "Error retrieving user profile",
          life: 2000
        });
      } finally {
        this.isLoading = false;
      }
    },
    async fetchSimulations(userId) {
      try {
        const currentUserIdResponse = await axios.get(`/users/profile`);
        const currentUserId = currentUserIdResponse.data.id;

        if (userId === currentUserId) {
          const response = await axios.get(`/simulations/mine`);
          this.simulations = response.data;

          for (let i = 0; i < this.simulations.length; i++) {
            this.simulations[i].populationSize = this.simulations[i].population;
            this.simulations[i].commuterCosts = this.simulations[i].costs;
            this.simulations[i].averageIncome = this.simulations[i].income;
          }

        }
      }catch (error) {
        console.error("Error retrieving simulations:", error);
        this.toast.add({
          severity: "error",
          position: "top-center",
          detail: "Error retrieving statistics",
          life: 2000
        });
      }
    },
    roundDecimal(number) {
      return parseFloat(number.toFixed(3));
    },
    toggleButton(panelIndex) {
      this.panelsCollapsed[panelIndex] = !this.panelsCollapsed[panelIndex];
    },
    formatNumber(value) {
      if (typeof value !== 'number') return value;
      const options = {
        minimumFractionDigits: 0,
        maximumFractionDigits: 2
      };
      return value.toLocaleString('en-GB', options);
    }
  },
  computed: {
    userInitials() {
      const firstNameInitial = this.userFirstName.charAt(0).toUpperCase();
      const lastNameInitial = this.userLastName.charAt(0).toUpperCase();
      return `${firstNameInitial}${lastNameInitial}`;
    },
    simulationPanels() {
      return [
        {
          header: 'Simulations:',
          value: this.numberOfSimulations,
          toggleable: true,
          subHeader: 'Simulations name:',
          dataField: 'name'
        },
        {
          header: 'Average income:',
          value: this.roundDecimal(this.averageIncome),
          toggleable: true,
          subHeader: 'Income:',
          dataField: 'averageIncome',
          formattedField: simulation => {
            const income = simulation.averageIncome || {};
            return `${this.formatNumber(income.value)} ${income.currency || ''}`;
          }
        },
        {
          header: 'Average commuter cost:',
          value: this.roundDecimal(this.averageCommuterCosts),
          toggleable: true,
          subHeader: 'Commuter costs:',
          dataField: 'commuterCosts',
          formattedField: simulation => {
            const costs = simulation.commuterCosts || {};
            return `${this.formatNumber(costs.value)} ${costs.currency || ''}`;
          }
        },
        {
          header: 'Average population size:',
          value: this.roundDecimal(this.averagePopulationSize),
          toggleable: true,
          subHeader: 'Population size:',
          dataField: 'populationSize'
        }
      ];
    },
    isCurrentUser() {
      return this.user.id === this.$route.params.id;
    },
  }
};
</script>

<style scoped>

</style>

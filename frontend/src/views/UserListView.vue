<template>
  <div class="w-full">
    <div v-for="(user,index) in users" :key="user.id" class="card text-left card w-3/4 p-1 m-2 mx-auto w-full max-w-[750px]">
      <Panel :collapsed="true" toggleable >
        <template #togglericon  >
          <span class="p-2.5" :class="!panelsCollapsed[index] ? 'pi pi-chevron-down' : 'pi pi-chevron-up' " @click="toggleButton(index)"  ></span>
        </template>
        <slot />
        <template #header>
          <div class="flex items-center gap-2">
            <span class="font-bold"> {{ user.firstName + ' ' + user.lastName }}</span>
          </div>
        </template>
        <template #icons>
          <Button class="icon-button hover:bg-surface-100 dark:hover:bg-surface-800/80 " @click="goToUserProfile(user.id)" severity="secondary" outlined >
            <Icon icon="material-symbols:person-search" class="icon text-surface-600 dark:text-surface-0/80"/>
          </Button>
        </template>
        <div class="user-details flex">
          <ul>
            <li>
              <h2 class="pb-1.5"> User details: </h2>
            </li>
            <li class="user-field flex gap-1.5">
              <Icon icon="mdi:email" class="icon w-5 h-5"/>
              <strong> Email:</strong>
              <span> {{ user.email }}</span>
            </li>
            <li class="user-field flex items-center gap-1.5 ">
              <Icon icon="material-symbols:location-city-rounded" class="icon w-5 h-5"/>
              <strong> Number of simulations:</strong> {{ user.count }}
            </li>
          </ul>
        </div>
      </Panel>
    </div>
  </div>
</template>

<script>
import UserList from "@/components/UserList.vue";
import Panel from 'primevue/panel';
import Card from 'primevue/card';
import { Icon } from "@iconify/vue";
import Button from 'primevue/button';
import "vue3-toastify/dist/index.css";

export default {
  name: "UserListView",
  components: {
    UserList,
    Icon,
    Panel,
    Card,
    Button
  },
  data() {
    return {
      panelsCollapsed: []
    }
  },
  computed: {
    users() {
      return this.$store.state.users;
    }
  },
  methods: {
    // A method used to navigate to the UserProfileView page
    goToUserProfile(userId) {
      this.$router.push({ name: 'profile', params: { id: userId } });
    },
    async fetchUsers() {
      try {
        await this.$store.dispatch("fetchUsers");
      } catch (error) {
        console.error('Error fetching users:', error);
        throw new Error("Failed to fetch users");
      }
    },
    toggleButton(panelIndex) {
      this.panelsCollapsed[panelIndex] = !this.panelsCollapsed[panelIndex];
    }
  },
  // This is called by Vue every time the component is mounted.
  async mounted() {
    await this.fetchUsers();
  }
}
</script>

<style scoped>
.icon-button {
  padding: 0;
  border: none;
  cursor: pointer;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  justify-content: center;
  transition: background-color 0.3s;
}


</style>

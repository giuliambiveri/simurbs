<script>


export default {
  name: "Navbar",
  data() {
    return {
      links: [
        { to: "/users", label: "Users" },
        { to: "/add-simulation", label: "Add Simulation" },
        { to: "/simulations/mine", label: "My Simulations" }
      ],
      activeIndex: 0,
      isMenuOpen: false
    };
  },
  created() {
    this.setActiveTab(this.$route.path);
  },
  watch: {
    '$route'(to) {
      this.setActiveTab(to.path);
    }
  },
  methods: {
    setActiveTab(path) {
      if ((path.startsWith('/users/') && path.includes('/profile')) || (path === '/profile')) {
        this.activeIndex = -1;
      } else if (path.startsWith('/simulations/')) {
        this.activeIndex = 2;
      }
      else {
        const index = this.links.findIndex(link => link.to === path);
        if (index !== -1) {
          this.activeIndex = index;
        } else {
          this.activeIndex = 0;
        }
      }
    },
    toggleMenu() {
      this.isMenuOpen = !this.isMenuOpen;
    },
    handleLinkClick(index) {
      this.activeIndex = index;
      this.$router.push(this.links[index].to);
      this.isMenuOpen = false;
    },
    conditionalToggleMenu() {
      if (this.isMenuOpen) {
        this.toggleMenu();
      }
    }
  }
}
</script>

<template>
  <div class="fixed inset-x-0 top-0 z-10">
    <div class="flex justify-between items-center bg-surface-100 dark:bg-surface-900 px-4 py-3">
      <div class="sm:hidden">
        <button @click="toggleMenu">
          <svg class="w-6 h-10 text-black-600 dark:text-white" stroke="currentColor" viewBox="0 -2 24 24" xmlns="http://www.w3.org/2000/svg">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 13h16M4 20h16"></path>
          </svg>
        </button>
      </div>

      <div class="flex items-center sm:flex">
        <img alt="SimUrbs logo" src="/favicon.ico" width="40" height="45">
        <strong class="text-lg font-bold ml-2.5 text-primary-0 dark:text-primary-50">SimUrbs</strong>
      </div>


      <div class="hidden sm:block">
        <ul class="flex space-x-8">
          <li v-for="(link, index) in links" :key="index">
            <router-link :to="link.to" class="cursor-pointer px-3 pb-3 text-primary-0 dark:text-primary-50" :class="{ 'border-b-2 font-bold border-primary-0 dark:border-primary-50': activeIndex === index }" @click.native="handleLinkClick(index)">
              {{ link.label }}
            </router-link>
          </li>
        </ul>
      </div>

      <router-link to="/profile" @click="conditionalToggleMenu">
        <i class="text-primary-0 dark:text-primary-50 pi pi-user" style="font-size: 1.5rem;"></i>
      </router-link>
    </div>

    <div v-if="isMenuOpen" class="bg-surface-100 dark:bg-surface-900 px-4 py-2 sm:hidden">
      <div class="flex flex-col space-y-2 text-left">
        <router-link v-for="(link, index) in links" :key="index" :to="link.to" class="block text-primary-0 dark:text-white " :class="{ ' font-extrabold': activeIndex === index }" @click="handleLinkClick(index)">
          {{ link.label }}
        </router-link>
      </div>
    </div>
  </div>
</template>

<style scoped>


</style>

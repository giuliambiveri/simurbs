<template>
  <div class="grid dark:bg-surface-800" v-if="showNavbarAndFooter">
    <Navbar v-if="showNavbarAndFooter" />

    <div v-if="showNavbarAndFooter" style="height: 62px"></div>

    <div class="m-2">
      <div class="rounded-lg overflow-hidden">
        <router-view />
      </div>
    </div>

    <Footer v-if="showNavbarAndFooter" />
  </div>
  <div v-else>
      <div style="height:100%;width:100%;overflow:hidden">
        <router-view />
      </div>
  </div>

  <Loader v-if="isLoading" />
  <ConfirmDialog />
  <Toast position="top-center" />
</template>

<script>
import Navbar from "@/components/Navbar.vue";
import Footer from "@/components/Footer.vue";
import Loader from "@/components/Loader.vue";
import Nav from "@/components/Navbar.vue";
import ConfirmDialog from "primevue/confirmdialog";
import Toast from "primevue/toast";

export default {
  components: {
    Nav,
    Loader,
    Navbar,
    Footer,
    ConfirmDialog,
    Toast
  },
  data() {
    return {
      isLoading: false,
      isLoginPage: false,
      isCitySimulationView: false
    };
  },
  computed: {
    showNavbarAndFooter() {
      const hiddenPages = ['login', 'simulationView', 'simulationPublicView'];
      return !hiddenPages.includes(this.$route.name);
    }
  }
};
</script>

<style scoped>
* {
  padding: 0;
  margin: 0;
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

.grid {
  min-height: 100vh;
  display: grid;
  grid-template-rows: auto 1fr auto;
}
</style>

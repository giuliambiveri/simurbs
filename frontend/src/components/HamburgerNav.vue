<script>
import {Icon} from "@iconify/vue";

export default {
  name: "HamburgerNav",
  components: {Icon},
  data() {
    return {
      isMenuOpen: false
    };
  },
  methods: {
    toggleMenu() {
      this.isMenuOpen = !this.isMenuOpen;
      if (document.querySelector(".modal")) {
        if (this.isMenuOpen) {
          document.querySelector(".modal").style.zIndex = -1;
        } else {
          document.querySelector(".modal").style.zIndex = 0;
        }
      }
    }
  }
}
</script>

<template>
<div class="container">
  <Icon class="menu-toggler" @click="toggleMenu" :icon="isMenuOpen ? 'material-symbols:close-rounded' : 'material-symbols:menu-rounded'" width="30" />

  <div class="menu-container" v-if="isMenuOpen">
    <div>
      <router-link @click="isMenuOpen=false" class="menu-item" to="/users">Users</router-link>
      <router-link @click="isMenuOpen=false" class="menu-item" to="/add-simulation">Add Simulation</router-link>
      <router-link @click="isMenuOpen=false" class="menu-item" to="/simulations/mine">My Simulations</router-link>
    </div>
  </div>
</div>
</template>

<style scoped>
.container {
  display: none;
}

@media only screen and (max-width: 900px) {
  .container {
    display: block;
  }
}

.menu-toggler {
  z-index: 2;
  cursor: pointer;
}

.menu-container {
  z-index: 1;
  position: absolute;
  left: 0;
  top: 60px;
  height: calc(100vh - 60px);
  background: white;
  width: 100vw;
}

.menu-container > div {
  position: absolute;
  top: 45%;
  left: 50%;
  transform: translate(-50%, -50%);
  display: grid;
  gap: 10px;
  width: calc(100% - 40px);
}

.menu-container > div > * {
  color: black;
  font-weight: bold;
  text-decoration: none;
  border-radius: 10px;
  background-color: #eee;
  padding: 20px;
}
</style>
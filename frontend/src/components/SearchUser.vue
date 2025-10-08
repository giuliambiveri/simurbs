<template>
  <transition name="fade">
  </transition>
</template>

<script>
export default {
  name: "SearchUser",
  props: {
    isVisible: Boolean
  }
}
</script>


<style scoped>
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.5s;
}
.fade-enter, .fade-leave-to {
  opacity: 0;
}
.popup {
  position: fixed; /* or absolute, depending on your layout */
  top: 60px; /* Adjust based on the navbar's height and desired gap */
  right: 20px; /* Adjust to align with the magnifier icon */
  background-color: white; /* Background color for visibility */
  border: 1px solid #ccc; /* Optional border */
  padding: 10px;
  width: 300px; /* Adjust width as needed */
  box-shadow: 0 2px 5px rgba(0,0,0,0.2); /* Optional shadow for depth */
  z-index: 1000; /* Ensure it's above other content */
}

</style>
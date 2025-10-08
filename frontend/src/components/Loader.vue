<script>
export default {
  name: "Loader"
}
</script>

<template>
  <div class="outer">
  </div>
  <div class="inner">
    <div class="loader"></div>
  </div>
</template>

<style scoped>
.outer {
  z-index: 20;
  top: 0;
  left: 0;
  position: fixed;
  width: 100%;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.5);
}

.inner {
  z-index: 21;
  position: fixed;
  top: 45%;
  left: 50%;
  transform: translate(-50%, -50%);
  padding: 50px;
  min-width: 100px;
  background: lightgrey;
  font-size: 50px;
  border-radius: 10px;
  color: black;
}

.loader {
  width: 60px;
  aspect-ratio: 2;
  --_circle: no-repeat radial-gradient(circle closest-side,#000 90%,#0000);
  background:
      var(--_circle) 0%   50%,
      var(--_circle) 50%  50%,
      var(--_circle) 100% 50%;
  background-size: calc(100%/3) 50%;
  animation: l3 0.4s infinite linear;
}
@keyframes l3 {
  20%{background-position:0%   0%, 50%  50%,100%  50%}
  40%{background-position:0% 100%, 50%   0%,100%  50%}
  60%{background-position:0%  50%, 50% 100%,100%   0%}
  80%{background-position:0%  50%, 50%  50%,100% 100%}
}
</style>
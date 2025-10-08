<script>
import Card from 'primevue/card';
import Button from 'primevue/button';
import 'primeicons/primeicons.css'

export default {
  name: "NotFound",
  components: {
    Card,
    Button
  },
  methods: {
    goHomePage() {
      this.$router.push("/");
    }
  }
}
</script>

<template>
  <div
      class="absolute top-[50%] left-[50%] translate-x-[-50%] translate-y-[-60%] w-[80%] max-w-[430px] rounded-md overflow-hidden">
    <Card class="bg-surface-100 dark:bg-surface-800 text-center">
      <template #title>
        <h1 class="title">404</h1>
      </template>
      <template #subtitle>
        <h2 class="subtitle">Page Not Found</h2>
      </template>
      <template #content>
        <p class="m-0">
          Sorry, we couldn't find the page.
        </p>
        <p class="m-0">
          Click the button below to return to the home page.
        </p>
      </template>
      <template #footer>
        <Button class="back-home-button" label="Your Simulations" icon="pi pi-arrow-left" @click="goHomePage"/>
      </template>
    </Card>
  </div>
</template>

<style scoped>
.title {
  font-size: 2.5rem;
  margin-top: 10px;
}

.subtitle {
  font-size: 1.5rem;
  margin-top: 0;
}

.text-center {
  text-align: center;
}
</style>

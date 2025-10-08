<template>
  <div class="page-background fixed top-0 left-0 w-full h-full">
    <img src="/images/citySkyline.jpg" alt="Background Image" class="w-full h-full object-cover blur-sm dark:opacity-80"/>
  </div>
  <div class="header flex m-5 items-center">
    <img class="simUrbsLogo mr-2 z-10" alt="SimUrbs logo" src="/favicon.ico" width="40" height="45">
    <h1 class="title m-2.5 z-10 text-2xl">
      <strong class="dark:text-primary-50">
        <h1 class="text-black">SimUrbs</h1>
      </strong>
    </h1>
  </div>
  <div class="flex justify-center items-center absolute top-[50%] left-[50%] translate-x-[-50%] translate-y-[-50%]" >
    <div class="mx-auto flex  flex-col justify-center space-y-6 md:w-[450px] xs:w-auto">
      <Card class="card p-10 rounded-xl shadow-lg overflow-hidden dark:bg-sky-950/90">
        <template class="login-title"  #title >
          <div class="flex justify-center">
            <strong class="title-login text-bg-dark text-black dark:text-white">
              Login
            </strong>
          </div>
        </template>
        <template #content >
          <div class="modal">
            <div class="flex flex-col space-y-2 text-center">
              <p>
                <Button label="Primary" severity="contrast" id="button" class="login-button w-full text-black border-2 border-cyan-200 hover:border-2 dark:bg-sky-800 hover:border-sky-500 dark:text-white dark:border-sky-950 dark:hover:border-2 dark:hover:border-blue-100 dark:focus:border-2 dark:focus:border-blue-100" @click="redirectToLoginPage" outlined >Click to Log In</Button>
              </p>
            </div>
            <Div layout="horizontal" class="or-continue-with md:flex text-white justify-center pt-4 pb-4" align="center">
              <strong class="text-bg-dark text-black dark:text-white">
                OR CONTINUE WITH
              </strong>
            </Div>
              <FloatLabel>
                <InputText class="input w-full bg-sky-200 text-black border-2 border-cyan-200 hover:border-2 hover:border-blue-300 focus:border dark:text-white dark:border-sky-950/70 dark:hover:border-2 dark:hover:border-blue-100 dark:focus:border-2 dark:focus:border-blue-100" id="url" v-model="url" />
                <label for="url">Enter URL here</label>
              </FloatLabel>
            <Button label="Contrast" severity="contrast" id="button" class="submit-button w-full text-black border-2 border-cyan-200 hover:border-2 hover:border-blue-300 focus:border dark:bg-sky-800 dark:text-white dark:border-sky-950 dark:hover:border-2 dark:hover:border-blue-100 dark:focus:border-2 dark:focus:border-blue-100" @click="redirectToPublicPage" :disabled="!url.trim()" outlined> Continue </Button>
          </div>
        </template>
      </Card>
    </div>
  </div>
</template>


<script>
import "vue3-toastify/dist/index.css";
import axios from "../axios";

import FloatLabel from "primevue/floatlabel";
import InputText from "primevue/inputtext";
import Button from 'primevue/button';
import Divider from "primevue/divider";
import Card from 'primevue/card';
import Image from 'primevue/image';
import {useToast} from "primevue/usetoast";



export default {
  name: "LoginPage",
  components: {
    FloatLabel,
    InputText,
    Button,
    Divider,
    Card,
    Image
  },
  data() {
    return {
      url: '',
      toast: useToast()
    }
  },
  methods: {
    redirectToLoginPage() {
      window.location.href = `${process.env.VUE_APP_BASE_URL}/oauth2/authorization/switch-edu-id`;
    },
    async redirectToPublicPage() {
      const parts = this.url.split('/');
      if (parts[parts.length - 2] === 'public') {
        const extractedId = parts[parts.length - 1];
        try {
          let response = await axios.get(`/simulations/public/${extractedId}`);
          window.location.href = `${window.location.origin}/simulations/public/` + extractedId;
        } catch (error) {
          if (error.response.status === 404) {
            this.toast.add({
              severity: 'error',
              summary: 'Error 404',
              position: "top-center",
              detail: 'Simulation not found',
              life: 3000
            });
          } else {
            console.log(error);
            this.toast.add({
              severity: 'error',
              summary: 'Unknown error',
              position: "top-center",
              detail: 'Please try again with a valid url',
              life: 3000
            });
          }
        }
      }
      else{
        this.toast.add({
          severity: 'warn',
          summary: 'Invalid URL',
          position: "top-center",
          detail: 'Simulation not found',
          life: 3000
        });
      }

    }
  }
}

</script>

<style scoped>
.card {
  background-color: rgba(224, 242, 254, 0.95);
}

.title-login{
  font-size: 40px;
}

.login-button{
  margin: 5px 0px;
}

button {
  background-color: rgba(125, 211, 252, 1)
}

button:hover {
  background-color: rgb(94, 178, 246);
}

.input {
  margin: 5px 0px;
  background-color: rgba(125, 211, 252, 0.7);
  border-color: rgb(165 243 252);
  border-width: 2px;
}

input:hover {
  border-width: 2px;
  border-color: rgb(14 165 233);
  background-color: rgba(125, 211, 252, 1);
}

input:focus {
  border-width: 2px;
  box-shadow: none !important;
  border-color: rgb(94, 178, 246);
  background-color: rgba(125, 211, 252, 1);
}

.submit-button {
  margin: 5px 0px;
}

@media (prefers-color-scheme: dark) {

  .card {
    background-color: rgba(8, 47, 73, 0.9);
  }

  .input {
    margin: 5px 0px;
    background-color: rgba(7, 89, 133, 0.7);
    border-color: rgb(8 47 73);
    border-width: 2px;
  }

  input:hover {
    border-width: 2px;
    border-color: rgb(8 47 73);
    background-color: rgba(7, 89, 133, 1);
  }

  input:focus {
    border-width: 2px;
    box-shadow: none !important;
    border-color: rgb(8 47 73);
    background-color: rgba(7, 89, 133, 1);
  }

  button {
    background-color: rgb(7, 89, 133);
  }

  button:hover {
    background-color: rgb(3 105 161);
    border-color: rgb(8 47 73);
  }
}

</style>

import { createRouter, createWebHistory } from 'vue-router'
import axios from "axios";

const routes = [
  {
    path: '/',
    name: 'home',
    redirect: '/simulations/mine',
  },
    {
        path: '/login',
        name: 'login',
        component: () => import(/* webpackChunkName: "login" */ '../views/LoginPage.vue')
    },
  {
    path: '/users',
    name: 'users',
    component: () => import(/* webpackChunkName: "users" */ '../views/UserListView.vue')
  },
  {
    path: '/simulations/:id/edit',
    name: 'edit',
    component: () => import(/* webpackChunkName: "edit" */ '../views/AddEditSimulationView.vue')
  },
  {
    path: '/users/:id/profile',
    name: 'profile',
    component: () => import(/* webpackChunkName: "profile" */ '../views/UserProfileView.vue'),
    props: true
  },
  {
    path: '/profile',
    name: 'userprofile',
    component: () => import(/* webpackChunkName: "userprofile" */ '../views/UserProfileView.vue')
  },
  {
    path: '/simulations/mine',
    name: 'mysimulations',
    component: () => import(/* webpackChunkName: "mysimulations" */ '../views/MySimulationsView.vue')
  },
  {
    path: '/add-simulation',
    name: 'add-simulation',
    component: () => import(/* webpackChunkName: "add-simulation" */ '../views/AddEditSimulationView.vue')
  },
  {
    path: '/simulations/:id', // Dynamic path at the same level
    name: 'simulationView',
    component: () => import(/* webpackChunkName: "simulationDetail" */ '../views/CitySimulationView.vue'),
    children: [
      {
        path: ':x/:y',
        component: () => import(/* ... */ '../components/BlockInfoBox.vue')
      }
    ]
  },
  {
    path: '/simulations/public/:id',
    name: 'simulationPublicView',
    component: () => import(/* webpackChunkName: "publicSimulationDetail" */ '../views/CitySimulationView.vue'),
    meta: { public: true },
    children: [
      {
        path: ':x/:y',
        component: () => import(/* ... */ '../components/BlockInfoBox.vue')
      }
    ]
  },
  {
    path: '/:catchAll(.*)', // Define a parameter named "catchAll" with a custom regular expression
    name: 'catchAll',
    component: () => import(/* webpackChunkName: "not-found" */ '../views/NotFound.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

axios.defaults.baseURL = `${process.env.VUE_APP_BASE_URL}`;

router.beforeEach(async (to, from, next) => {

  // Check if the route is marked as public
  if (to.meta.public) {
    next();
    return;
  }

  try {
    // Check if the current route is /login
    if (to.path === '/login') {
      // If user is already logged in, redirect to home
      const response = await axios.get('/users/check-authentication', { withCredentials: true });
      if (response.data.loggedIn === true) {
        next({ name: 'home' });
      } else {
        next();
      }
      return;
    }

    // Check user authentication before each route navigation
    const response = await axios.get('/users/check-authentication', { withCredentials: true });
    console.log(response.data)
    if (response.data.loggedIn === true) {
      // If user is logged in, allow the navigation to proceed
      next();
    } else {
      // Redirect to login page if user is not logged in
      next({ name: 'login' });
    }
  } catch (error) {
    // Handle authentication check errors
    console.error('Authentication check error:', error);
    // Redirect to login page or handle error as needed
    next({ name: 'login' });
  }
});


export default router

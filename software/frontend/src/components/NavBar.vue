<template>
  <div>
    <b-navbar fixed="true" toggleable="md" type="light" variant="light" class="navbar">

      <b-navbar-toggle target="nav_collapse"></b-navbar-toggle>

      <b-navbar-brand :to="'/'"><b-img width="170px" :src="require('../assets/logo.svg')" alt="SWARMcomposer Logo" /></b-navbar-brand>

      <b-collapse is-nav id="nav_collapse">

        <b-navbar-nav class="big" style="margin-left: 20px">
          <b-nav-item :to="'/'">Workspace</b-nav-item>
          <b-nav-item :to="'/admin'" v-if="isAdmin">Adminpanel</b-nav-item>
        </b-navbar-nav>
        <b-navbar-nav class="ml-auto">
          <b-button class="mr-3" :pressed="false" @click="$emit('admin')" v-if="!isAdmin" variant="success">Admin</b-button>
          <b-button class="mr-3" :pressed="true" @click="$emit('admin')" v-else variant="danger">Admin</b-button>
          <b-nav-item :to="'/login'" v-if="!loggedIn">Login</b-nav-item>
          <b-nav-item-dropdown right v-else>
            <template slot="button-content">
              logged in as {{email}}
            </template>
            <b-dropdown-item @click="$emit('logout')">Logout</b-dropdown-item>
          </b-nav-item-dropdown>
        </b-navbar-nav>

      </b-collapse>

    </b-navbar>
  </div>
</template>

<script>

export default {
  data() {
    return {

    }
  },
  props: {
    isAdmin: Boolean,
    email: String,
    loggedIn: Boolean
  }
}
</script>

<style scoped>
  .navbar {
    height: 10vh;
  }

</style>

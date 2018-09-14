<template>
  <div>
    <b-navbar fixed="top"
              toggleable="md"
              type="light"
              variant="light"
              class="navbar">
      <b-container>
      <b-navbar-toggle target="nav_collapse"></b-navbar-toggle>
      <b-navbar-brand :to="'/'">
        <b-img height="40vh"
               :src="require('../assets/logo.svg')"
               alt="SWARMcomposer Logo" />
        </b-navbar-brand>
      <b-collapse is-nav id="nav_collapse">
        <b-navbar-nav class="big" style="margin-left: 20px">
          <b-nav-item :to="'/'">Workspace</b-nav-item>
          <b-nav-item :to="'/admin'" v-if="user.isAdmin">Adminpanel</b-nav-item> <!-- && isLoggedIn -->
        </b-navbar-nav>
        <b-navbar-nav class="ml-auto">


          <!-- später raus -->
          <b-button class="mr-3" :pressed="false" @click="$emit('admin')" v-if="!user.isAdmin" variant="success">Admin</b-button>
          <b-button class="mr-3" :pressed="true" @click="$emit('admin')" v-else variant="danger">Admin</b-button>


          <b-nav-item :to="'/login'" v-if="!user.loggedIn">Login</b-nav-item>
          <b-nav-item-dropdown right v-else>
            <template slot="button-content">
              Sie sind eingeloggt als: {{ user.fullName }}
            </template>
            <b-dropdown-item @click="$emit('logout')">Logout</b-dropdown-item>
          </b-nav-item-dropdown>
        </b-navbar-nav>
      </b-collapse>
    </b-container>
    </b-navbar>
  </div>
</template>

<script>
export default {
  props: {
    user: Object
  }
}
</script>

<style scoped>
.navbar {
  height: 8vh;
}

</style>

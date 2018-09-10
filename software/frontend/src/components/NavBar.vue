<template>
  <div>
    <b-navbar fixed="true" toggleable="md" type="light" variant="light" class="navbar">

      <b-navbar-toggle target="nav_collapse"></b-navbar-toggle>

      <b-navbar-brand @click="$emit('workspace')" href="#"><b-img width="170px" :src="require('../assets/logo.svg')" alt="SWARMcomposer Logo" /></b-navbar-brand>

      <b-collapse is-nav id="nav_collapse">

        <b-navbar-nav class="big" style="margin-left: 20px">
          <b-nav-item @click="$emit('workspace')">Workspace</b-nav-item>
          <b-nav-item @click="$emit('adminpanel')">AdminPanel</b-nav-item>
        </b-navbar-nav>

        <!-- Right aligned nav items -->
        <b-navbar-nav class="ml-auto">
          <b-nav-item @click="$emit('login')">Login</b-nav-item>
          <b-nav-item-dropdown right>
            <!-- Using button-content slot -->
            <template slot="button-content">
              logged in as {{userId}}
            </template>
            <b-dropdown-item href="#">Signout</b-dropdown-item>
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
      userId: 0,
      loggedIn: false
    }
  }
}
</script>

<style scoped>
  .big {
    font-size: 30px;
  }
  .navbar {
      fixed: true;
  }

</style>

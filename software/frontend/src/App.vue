<template>
  <div>
    <NavBar :admin="admin" :loggedIn="loggedIn" :email="email" @logout="logout" @admin="admin" :isAdmin="isAdmin"/>
    <router-view @login="login"></router-view>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar'

export default {
  data() {
    return {
      loggedIn: false,
      email: '',
      isAdmin: false
    }
  },
  name: 'app',
  components: {
    NavBar
  },
  methods: {
    login() {
      this.loggedIn = true
    },
    logout() {
      this.loggedIn = false
    },
    admin() {
      this.isAdmin = !this.isAdmin
    }
  }
}
</script>

<style>
.mainlayout {
  margin: 10vh auto;
  width: 90vw;
  padding: 2.5% 5%;
  background-color: ;
}
.round {
  border-radius: 10px;
}
</style>

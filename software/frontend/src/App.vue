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
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
.mainlayout {
  margin: 10vh auto;
  width: 60vw;
  padding: 2.5% 5%;
  background-color: lightgrey;
}
.round {
  border-radius: 10px;
}
</style>

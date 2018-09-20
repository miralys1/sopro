<template>
  <div>
    <!-- navbar -->
    <NavBar :user="user" @logout="logout"/>
    <!-- works like an iframe -->
    <router-view @login="login" :user="user"></router-view>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar'

export default {
  data() {
    return {
      user: {
        loggedIn: false,
        userId: -1,
        isAdmin: false,
        fullName: ''
      }
    }
  },
  name: 'app',
  components: {
    NavBar
  },
  methods: {
    // on login set state
    login(user) {
      this.user.loggedIn = true
      this.user.id = user.id
      this.user.isAdmin = user.isAdmin
      this.user.fullName = user.fullName
    },
    // logout event
    logout() {
      // request
      this.axios({
        method: 'get',
        url: '/logout'
      }).then(res => {
        // if successful delete local state
        this.user.loggedIn = false
        this.user.id = -1
        this.user.isAdmin = false
        this.user.fullName = ''
        // otherwise don't do anything
      }).catch(res => alert('Etwas ist schiefgelaufen'))

    }
  },
  // on mount, make request and check if cookie is still valid
  mounted() {
    this.axios({
      url: '/authentification',
      method: 'get',
    }).then(res => {
      // if valid cookie, set state accordingly
      this.user.loggedIn = true
      this.user.id = res.data.id
      this.user.isAdmin = res.data.admin
      this.user.fullName = res.data.fullName
    }).catch(function(err) {console.log(err)})
  }
}
</script>

<style>
.mainlayout {
  margin: 10vh auto;
  width: 65vw;
  padding: 2.5% 2.5%;
  background-color: #f8f9fa;
  border-radius: 5px;
  border: 1px solid black;
}
.round {
  border-radius: 10px;
}
</style>

<template>
  <!-- tabs to choose between the different admin actions with v-if to reset them -->
  <div class="mainlayout" v-if="show">
  <b-card no-body>
  <b-tabs pills card vertical>
    <b-tab title="Dienste aus JSON laden" active @click="toggle">
      <JsonEinlesen v-if="toggly"/>
    </b-tab>
    <b-tab title="Dienst manuell hinzufügen" @click="toggleUpdate">
      <AdminDienstForm  v-if="toggly" v-bind:tupdate="update" />
    </b-tab>
    <b-tab title="Dienste bearbeiten" @click="toggleUpdate">
      <AdminDienstBearbeiten v-if="toggly" v-bind:pupdate="update" />
    </b-tab>
    <b-tab title="Nutzer bearbeiten" @click="toggle">
      <AdminUserBearbeiten v-if="toggly" v-bind:loggedInUser="user" />
    </b-tab>
  </b-tabs>
</b-card>
</div>
</template>

<script>
import AdminDienstForm from '@/components/AdminDienstForm'
import JsonEinlesen from '@/components/JsonEinlesen'
import AdminDienstBearbeiten from '@/components/AdminDienstBearbeiten'
import AdminUserBearbeiten from '@/components/AdminUserBearbeiten'

export default {

  mounted() {
    //preventing people from accessing /admin by entering it directly
    this.axios.get('/authentification')
             .then(response => {
               var us = response.data;
               if(us.admin == false){
                 this.$router.push("/");
                 alert("Sie sind nicht berechtigt auf diese Seite zuzugreifen.");
               }
               this.show = true;
           }
                 )
             .catch(error => {
              this.$router.push("/");
              alert("Sie sind nicht berechtigt auf diese Seite zuzugreifen.");
                 });

  },

  props: ['user'],
  data() {
    return {
      toggly: true,
      update: true,
      show: false,
    }
  },
   components: { AdminDienstForm, JsonEinlesen, AdminDienstBearbeiten, AdminUserBearbeiten

   },
   methods: {
     toggle() {
       this.toggly = false;
       this.$nextTick(() => { this.toggly = true });
     },

     toggleUpdate() {
       //provoke change in AdminDienstBearbeiten
       this.toggle;
       this.update = !this.update;
     }


   }
}
</script>

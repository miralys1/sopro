<template>
  <div class="mainlayout">
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

  props: ['user'],
  data() {
    return {
      toggly: true,
      update: true,
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
       this.toggle;
       this.update = !this.update;
     }


   }
}
</script>

<style scoped="true">
</style>

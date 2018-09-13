<template>
  <div class="mainlayout">
  <b-card no-body>
  <b-tabs pills card vertical>
    <b-tab title="Dienste aus JSON landen" active @click="toggle">
      <JsonEinlesen v-if="toggly"/>
    </b-tab>
    <b-tab title="Dienst manuell bearbeiten" @click="toggle">
      <AdminDienstForm />
    </b-tab>
    <b-tab title="Dienste bearbeiten" @click="toggleUpdate">
      <AdminDienstBearbeiten v-if="toggly" v-bind:pupdate=update />
    </b-tab>
  </b-tabs>
</b-card>
</div>
</template>

<script>
import AdminDienstForm from '@/components/AdminDienstForm'
import JsonEinlesen from '@/components/JsonEinlesen'
import AdminDienstBearbeiten from '@/components/AdminDienstBearbeiten'

export default {
  data() {
    return {
      toggly: true,
      update: true
    }
  },
   components: { AdminDienstForm, JsonEinlesen, AdminDienstBearbeiten

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

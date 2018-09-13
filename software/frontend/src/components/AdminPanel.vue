<template>
  <div class="mainlayout">
  <b-card no-body>
  <b-tabs pills card vertical>
    <b-tab title="Load Services from JSON" active>
      <JsonEinlesen />
    </b-tab>
    <b-tab title="Manually entering a service">
      <AdminEingDienste />
    </b-tab>
    <b-tab title="Tab 3">
      Tab Contents 3
    </b-tab>
  </b-tabs>
</b-card>
</div>
</template>

<script>
import AdminEingDienste from '@/components/AdminEingDienste'
import JsonEinlesen from '@/components/JsonEinlesen'

export default {
  data() {
    return {
    }
  },
   components: { AdminEingDienste, JsonEinlesen

   }
}
</script>

<style scoped="true">
</style>

<template>
<div>

<div>
  <h3> {{this.msg}} </h3>
  <div class="list-group list-group-flush" style="overflow:scroll; height:400px;">
  <button type="button" class="list-group-item list-group-item-action" style="display: inline-block" v-for="(service,index) in services" @click="onClick(index)">
    {{service.name}} ({{service.version}}) {{service.organisation}}
  </button>
</div>
</div
<br><br>
<AdminDienstForm v-if="serviceSelected" v-bind:pform=selectedService v-bind:pedit='true'/>

</div>

</template>

<script>

import AdminDienstForm from '@/components/AdminDienstForm'

export default {

watch: {
  pupdate: function () {
    this.mupdate()
      }
    },
components: {AdminDienstForm},
props: ['pupdate'],
methods: {
  onClick(index){
    //creating a copy of the object to not pass a reference
    this.selectedService = JSON.parse(JSON.stringify(this.services[index]));
    this.serviceSelected = true;
  },

  mupdate(){
    this.axios.get('http://134.245.1.240:9061/composer-0.0.1-SNAPSHOT/services')
             .then(response =>
             this.services = response.data
                 )
             .catch(function (error) {
              alert("Fehler");
             console.log(error);
                 });
  }


},


 data() {
   return{
     serviceSelected: false,
     selectedService: {
       id: 0,
       name: "Johanna",
       organisation: "test",
       version: "",
       date: 0,
       logo: "",
       tags: [""
       ],
     formatIn: [
       {
         id: 0,
         type: "",
         version: "",
         compatibilityDegree: ""
       }
     ],
     formatOut: [
       {
         id: 0,
         type: "",
         version: "",
         compatibilityDegree: ""
       }
     ]
   },
     services: [],
     msg: "Dienste"
   }

 }

}

</script>

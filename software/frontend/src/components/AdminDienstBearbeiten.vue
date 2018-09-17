<template>
<div>

<div>
  <input type="text" class="search" placeholder="Suche.." v-model="searchedService" @keyup.enter="search">
  <br><br>
  <h3> {{this.msg}} </h3>
  <ul class="list-group list-group-flush" style="overflow-y: scroll; max-height:200px;">
  <button type="button" class="list-group-item list-group-item-action" style="display: inline-block; margin: auto auto; min-height: 10vh;" v-for="(service,index) in foundServices" @click="onClick(index)">
    {{service.name}} ({{service.version}}) {{service.organisation}}
  </button>
</ul>
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
  },
  searchedService: function() {
    if(this.searchedService == ""){
      this.foundServices = this.services;
    }
  }
    },


components: {AdminDienstForm},
props: ['pupdate'],
methods: {
  onClick(index){
    //creating a copy of the object to not pass a reference
    this.selectedService = JSON.parse(JSON.stringify(this.foundServices[index]));
    this.serviceSelected = true;
  },

  mupdate(){
    // this.axios.get('http://134.245.1.240:9061/composer-0.0.1-AUTH/services')
    //          .then(response => {
    //          this.services = response.data;
              this.foundServices = this.services;
            //}
    //              )
    //          .catch(function (error) {
    //           alert("Fehler");
    //          console.log(error);
    //              });
  },

  search() {
    var options = {
    shouldSort: true,
    threshold: 0.6,
    location: 0,
    distance: 100,
    maxPatternLength: 32,
    minMatchCharLength: 1,
    keys: [
    "name",
    "organisation",
    "formatIn.type",
    "formatOut.type"
    ]
};
this.$search(this.searchedService, this.services, options).then(results => {
  this.foundServices = results
})
  }


},


 data() {
   return{
     searchedService: "",
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
     services: [{name: "johanna", version: "h", organisation: "adesso", formatIn:[{type: "pdf"}, {type: "axio"}]},{name: "susanne", version: "h", organisation: "pudel"},
      {name: "franzi", version: "h", organisation: "katze", formatIn:[{type: "axios"}, {type: "pdf"}]},{name: "philip", version: "h", organisation: "google"}],
     msg: "Dienste",
    foundServices: this.services,
   }

 }

}

</script>

<style>
.search {
    background: transparent;
    border: none;
    border-bottom: 2px solid #000000;
}
</style>

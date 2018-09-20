<template>
<div>
  <!-- radio buttons to select between certified and non-certified services -->
  <b-form-group >
     <b-form-radio-group id="btnradios"
                         buttons
                         button-variant="outline-primary"
                         size="lg"
                         v-model="cert"
                         :options="certOptions"
                         name="radioBtnOutline" />
   </b-form-group>

<div>
  <input type="text" class="search" placeholder="Suche.." v-model="searchedService" @keyup.enter="search" @click="serviceSelected=false">
  <br><br>
  <h3> {{this.msg}} </h3>

  <!-- dynamic generation of buttons to select a matching service out of the proposed ones -->
  <ul class="list-group list-group-flush" style="overflow-y: scroll; max-height:200px;">
  <button type="button" class="list-group-item list-group-item-action" style="display: inline-block; margin: auto auto; min-height: 10vh;" v-for="(service,index) in foundServices" @click="onClick(index)">
    {{service.name}} ({{service.version}}) {{service.organisation}}
  </button>
</ul>
</div
<br><br>
<!-- using the service form in editing mode (see pedit) and passing the selectedService so that it can be preloaded into the form-->
<AdminDienstForm v-if="serviceSelected" v-bind:tupdate="upd" v-bind:pform="selectedService" v-bind:pedit='true' v-on:noForm="mupdate"/>

</div>

</template>

<script>

import AdminDienstForm from '@/components/AdminDienstForm'

export default {

watch: {
  pupdate: function () {
    this.mupdate()
  },
  // when the search field is empty all preselected services are shown again
  searchedService: function() {
    if(this.searchedService == ""){
      this.foundServices = this.preSearched;
    }
  },
  //listening for changes of the radio button selection and filtering all services accordingly into the preSearched array
  cert: function() {
    if(this.cert == 1) {
      this.preSearched = this.services;
      this.foundServices = this.preSearched;
      this.serviceSelected = false;
      this.search();
    }
    if(this.cert == 2) {
      this.preSearched = this.services.filter(object => object.certified == "true");
      this.foundServices = this.preSearched;
      this.serviceSelected = false;
      this.search();
    }
    if(this.cert == 3) {
      this.preSearched = this.services.filter(object => object.certified == "false");
      this.foundServices = this.preSearched;
      this.serviceSelected = false;
      this.search();
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

    //do not replace this with this.upd = !this.upd as it will not trigger a change event
    this.upd = false;
    this.$nextTick(() => { this.upd = true });
  },

  mupdate(){
    //hide the form and request services from the server
    this.serviceSelected = false;
    this.axios.get('/services')
             .then(response => {
             this.services = response.data;
             if(this.cert == 1) {
               this.preSearched = this.services;
               this.foundServices = this.preSearched;
               this.serviceSelected = false;
               this.search();
             }
             if(this.cert == 2) {
               this.preSearched = this.services.filter(object => object.certified == "true");
               this.foundServices = this.preSearched;
               this.serviceSelected = false;
               this.search();
             }
             if(this.cert == 3) {
               this.preSearched = this.services.filter(object => object.certified == "false");
               this.foundServices = this.preSearched;
               this.serviceSelected = false;
               this.search();
             }
           }
                 )
             .catch(function (error) {
              alert("Fehler beim Abfragen der Dienste vom Server.");

                 });
  },

  search() {
  //preventing an empty search as no service would be shown
  if(this.searchedService != "") {
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
//approximate search with searchedService-String in preSearched-Array
this.$search(this.searchedService, this.preSearched, options).then(results => {
  this.foundServices = results
})
}
  }


},



 data() {
   return{
     upd: false,
     preSearched: [],
     //radio button options
     certOptions: [
       {text: "Alle", value: 1},{text: "Zertifizierte", value: 2},{text: "Nicht Zertifizierte", value:3}
     ],
     //all services selected (default)
     cert: 1,
     searchedService: "",
     serviceSelected: false,
     selectedService: {
       id: 0,
       name: "",
       certified: "",
       organisation: "",
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

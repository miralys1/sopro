<template>
<div>

<div>
  <h3> {{this.msg}} </h3>
  <div class="list-group list-group-flush">
  <button type="button" class="list-group-item list-group-item-action" v-for="(service,index) in services" @click="onClick(index)">
    {{service.name}} ({{service.version}}) {{service.organisation}}
  </button>
</div>
</div
<br><br>
<AdminDienstbearbeitenProps v-bind:pform=selectedService />

</div>

</template>

<script>
import AdminDienstbearbeitenProps from '@/components/AdminDienstbearbeitenProps'

export default {
components: {AdminDienstbearbeitenProps},

methods: {

  onClick(index){
    this.serviceSelected = true;
    this.selectedService = this.services[index];
  }


},

 data() {
   return{
     serviceSelected: false,
     selectedService: {
       id: 0,
       name: "Johanna",
       organisation: "",
       version: "",
       date: 0,
       logo: "",
       tags: [""
       ],
     inputFormats: [
       {
         id: 0,
         type: "",
         version: "",
         compatibilityDegree: ""
       }
     ],
     outputFormats: [
       {
         id: 0,
         type: "",
         version: "",
         compatibilityDegree: ""
       }
     ],
   },
     services: [{
        "id": 7,
         "name": "3D-Modeller",
         "organisation": "IGD",
         "version": "3",
         "date": 1531573788,
         "tags": ["3D", "Modeller", "IFC"],
         "logo": "IGD_Modeller.png",
         "formatIn":  [
             {
                 "id": 5,
                 "type": "IFC",
                 "version": "2x0",
                 "compatibilityDegree": "strict"
             },
             {
                 "id": 3,
                 "type": "gbXML",
                 "version": "2",
                 "compatibilityDegree": "strict"
             }
         ],
         "formatOut":
         [
             {
                 "id": 2,
                 "type": "IFC",
                 "version": "2x0",
                 "compatibilityDegree": "strict"
             },
             {
                 "id": 1,
                 "type": "DWG",
                 "version": "5",
                 "compatibilityDegree": "flexible"
             }
         ]
     },
     {
         "id": 2,
         "name": "TP Modeller",
         "organisation": "TP",
         "version": "2.0",
         "date": 1526994588,
         "tags": ["3D", "Modeller", "Visualisierung", "Modellierung"],
         "logo": "TP_Modeller_20.png",
         "formatIn":  [
             {
                 "id": 4,
                 "type": "IFC",
                 "version": "4",
                 "compatibilityDegree": "strict"
             },
             {
                 "id": 6,
                 "type": "FBX",
                 "version": "1.0",
                 "compatibilityDegree": "flexible"
             },
             {
                 "id": 8,
                 "type": "COBie",
                 "version": "",
                 "compatibilityDegree": "flexible"
             }
         ],
         "formatOut":
         [
             {
                 "id": 10,
                 "type": "DWG",
                 "version": "6",
                 "compatibilityDegree": "flexible"
             },
             {
                 "id": 8,
                 "type": "COBie",
                 "version": "",
                 "compatibilityDegree": "flexible"
             },
             {
                "id": 2,
                 "type": "BCF",
                 "version": "2.0",
                 "compatibilityDegree": "strict"
             }
         ]
     }],
     msg: "Dienste"
   }

 }

}

</script>

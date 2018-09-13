<template>
<div>
  <!-- Styled -->
  <b-form-file v-model="file" :state="Boolean(file)" placeholder="Choose a file or drop one in this box..."  accept=".json"></b-form-file>
  <p>
    {{numberOfServices}} Services were detected.
  </p>
  <b-button @click="sendJson">Import Services</b-button>

</div>
</template>

<script>
export default {
  data () {
    return {
      file: null,
      services: null,
      text: "default",
      numberOfServices: 0
    }
  },
  methods: {
    readFile: function () {
      try{
        if(this.file == null) throw "No file has been loaded.";
        const reader = new FileReader();
        reader.onload = this.setText;

        reader.readAsText(this.file);
      } catch(err) {
        alert(err);
      }
    },
    setText: function(e) {
      this.text = e.target.result;
      this.readJson();
    },
    readJson: function() {
      try{
        var start = this.text.indexOf('[');
        var end = this.text.lastIndexOf('}');
        this.text = this.text.slice(start, end);

        this.services = JSON.parse(this.text);
        var n = this.services.length;
        this.numberOfServices = n;
        var numberUnvalid = 0;
        var err = "";

        for(var i = 0; i < n; i++){
          var srvs = this.services[i];
          var unvalid = false;
          var errMsg = "The " + (i+1) + ". service is missing"

          if(srvs.name == null  || srvs.name == "") {errMsg = errMsg + " a name"; unvalid = true}
          if(srvs.organisation == null  || srvs.organisation == "") {errMsg = errMsg + " an organisation"; unvalid = true}
          if(srvs.version == null  || srvs.version == "") {errMsg = errMsg + " a version"; unvalid = true}
          if(srvs.date == null) {errMsg = errMsg + " a date"; unvalid = true}
          if(srvs.logo == null  || srvs.logo == "") {errMsg = errMsg + " a logo"; unvalid = true}

          if(unvalid) {numberUnvalid++, err = err + errMsg}
        }

        if(numberUnvalid != 0) {this.numberOfServices= this.numberOfServices - numberUnvalid; throw err}
      } catch(err) {
        alert("While parsing the JSON file the following error occured: " + err)
      }
    },
    sendJson: function() {
      this.axios.post('http://134.245.1.240:9061/composer-0.0.1-SNAPSHOT/services', this.text)
           .then(function (response) { alert(response);})
           .catch(function (error) {alert(error);});
    }
  },
  watch: {
    file: function() {
      this.readFile()
    }
  }
}
</script>

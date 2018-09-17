<template>
<div>
  <!-- Styled -->
  <b-form-file v-model="file" :state="!unvalid" placeholder="Choose a file or drop one in this box..."  accept=".json"></b-form-file>
  <br> <br>
  <div v-if="show">
    {{numberOfServices}} Dienste wurden erkannt.
  </div>
  <b-button @click="sendJson">Dienste importieren</b-button>

</div>
</template>

<script>
export default {
  data () {
    return {
      file: null,
      services: null,
      text: "default",
      numberOfServices: 0,
      unvalid: true,
      show: false
    }
  },
  methods: {
    readFile: function () {
      this.show = false;

      try{
        this.unvalid = false;
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
      this.show = true;

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
          var errMsg = "The " + (i+1) + ". service is missing:";

          if(srvs.name == null  || srvs.name == "") {errMsg = errMsg + "\n a name"; unvalid = true}
          if(srvs.organisation == null  || srvs.organisation == "") {errMsg = errMsg + " an organisation"; unvalid = true}
          if(srvs.version == null  || srvs.version == "") {errMsg = errMsg + " a version"; unvalid = true}
          if(srvs.date == null) {errMsg = errMsg + " a date"; unvalid = true}
          if(srvs.logo == null  || srvs.logo == "") {errMsg = errMsg + " a logo"; unvalid = true}
          if(srvs.tags == null || srvs.tags.length == 0) {errMsg = errMsg + " a tag"; unvalid = true}
          if((srvs.formatIn == null || srvs.formatIn.length == 0) && (srvs.formatOut == null || srvs.formatOut.length == 0))
          {errMsg = errMsg + " an in or output format"; unvalid = true}

          if(unvalid) {numberUnvalid++, err = err + errMsg+ '\n'}
        }

        if(numberUnvalid != 0) {this.numberOfServices= this.numberOfServices - numberUnvalid; throw err}
      } catch(err) {
        this.unvalid = true;

        if(Boolean(err.message.indexOf("SyntaxError"))) {
         alert("Die JSON Datei scheint fehlerhaft zu sein. Bitte achten Sie darauf nur richtige JSON datein hochzuladen.");
         this.show = false;
        } else {
         alert("While parsing the JSON file the following error occured: " + err);
      }
      }
    },
    sendJson: function() {

      if(!this.unvalid){
      this.axios.post('http://134.245.1.240:9061/composer-0.0.1-SNAPSHOT/services', this.text)
           .then(function (response) { alert(response);})
           .catch(function (error) {alert(error);});
      this.show = false;
      } else {
        alert("Die gelesene Datei ist nicht gültig");
      }
    }
  },
  watch: {
    file: function() {
      this.show = false;
      this.readFile()
    }
  }
}
</script>

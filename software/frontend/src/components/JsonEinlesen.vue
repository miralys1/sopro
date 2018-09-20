<template>
<div>

  <b-form-file v-if= "showBrowser" v-model="file" :state="!unvalid" placeholder="Wählen Sie eine JSON aus..."  accept=".json"></b-form-file>
  <br> <br>
  <div style= "float:left;" v-if="show">
    {{numberOfServices}} Dienste wurden erkannt.
  </div>
  <b-button style= "float:right;" @click="sendJson">Dienste importieren</b-button>

</div>
</template>

<script>
export default {
  data () {
    return {
      showBrowser: true,
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
        //reading JSON as text
        this.unvalid = false;
        if(this.file == null) throw "Es wurde keine Datei geladen.";
        const reader = new FileReader();
        reader.onload = this.setText;

        reader.readAsText(this.file);
      } catch(err) {
        alert(err);
      }
    },

    setText: function(e) {
      //setting text to the read string if loading was successfull
      this.text = e.target.result;
      this.readJson();
    },

    readJson: function() {
      this.show = true;

      try{
        //the server accepts a list of objects not a single object with an array
        var start = this.text.indexOf('[');
        var end = this.text.lastIndexOf('}');
        this.text = this.text.slice(start, end);

        this.services = JSON.parse(this.text);

        var n = this.services.length;
        this.numberOfServices = n;
        var numberUnvalid = 0;
        var err = "";

        //regarding all services to show a complete error string
        for(var i = 0; i < n; i++){
          var srvs = this.services[i];
          var unvalid = false;
          var errMsg = "Dem " + (i+1) + ". Dienst fehlt:";

          if(srvs.name == null  || srvs.name == "") {errMsg = errMsg + "\n ein Name"; unvalid = true}
          if(srvs.organisation == null  || srvs.organisation == "") {errMsg = errMsg + "\n eine Organisation"; unvalid = true}
          if(srvs.version == null  || srvs.version == "") {errMsg = errMsg + "\n eine Version"; unvalid = true}
          if(srvs.date == null) {errMsg = errMsg + "\n ein Datum"; unvalid = true}
          if(srvs.certified == null || srvs.certified == "") {errMsg = errMsg + "\n eine Zertifizierung"; unvalid = true}
          if(srvs.logo == null  || srvs.logo == "") {errMsg = errMsg + "\n ein Logo"; unvalid = true}
          if(srvs.tags == null || srvs.tags.length == 0) {errMsg = errMsg + "\n mindestens ein Tag"; unvalid = true}
          if((srvs.formatIn == null || srvs.formatIn.length == 0) && (srvs.formatOut == null || srvs.formatOut.length == 0))
          {errMsg = errMsg + " \n mindestens ein Ein- oder Ausgabeformat"; unvalid = true}

          if(unvalid) {numberUnvalid++, err = err + errMsg+ '\n'}
        }
        //determinating how many services are valid
        if(numberUnvalid != 0) {this.numberOfServices= this.numberOfServices - numberUnvalid; throw err}
      } catch(err) {
        this.unvalid = true;

        if(err instanceof SyntaxError) {
         alert("Die JSON Datei scheint fehlerhaft zu sein. Bitte achten Sie darauf nur richtige JSON Datein hochzuladen.");
         this.show = false;

        } else {
         alert("Beim Lesen der Datei sind folgende Fehler aufgetreten: \n" + err);
      }
      }
    },
    sendJson: function() {
      //only sending data to server if JSON was valid
      if(!this.unvalid){
      this.axios({
        url: '/services',
        method: 'post',
        data: this.services,
        headers: {
          'Content-Type': 'application/json'
        }
      }).then(response => { alert('Dienste erfolgreich eingelesen');
      //clearing brose field
                            this.showBrowser = false;
                            this.$nextTick(() => { this.showBrowser = true });})
           .catch(function (error) {alert('Irgendetwas ist schief gelaufen');});
      this.show = false;
      } else {
        alert("Die gelesene Datei ist nicht gültig.");
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

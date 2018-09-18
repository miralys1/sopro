<template>
  <div class = "out">
    <b-form @submit="onSubmit" @reset="onReset" v-if="show">
      <b-button type="submit" variant="primary" style="float:right;margin-left: 0.5vw;">Speichern</b-button>
      <b-button type="reset" variant="danger" style="float:right;">Reset</b-button>
      <h4>Dienst Informationen</h4>
      <b-form-group id="GeneralName"
                    label="Name:"
                    label-for="genName">
        <b-form-input id="genName"
                      type="text"
                      v-model="form.name"
                      required>
        </b-form-input>
      </b-form-group>

      <b-form-group id="GeneralOrg"
                    label="Organisation:"
                    label-for="genOrg">
        <b-form-input id="genOrg"
                      type="text"
                      v-model="form.organisation"
                      required>
        </b-form-input>
      </b-form-group>

      <b-form-group id="GeneralVersion"
                    label="Version:"
                    label-for="genVersion">
        <b-form-input id="genVersion"
                      type="text"
                      v-model="form.version"
                      required>
        </b-form-input>
      </b-form-group>

      <b-form-group id="Certified"
                    label="Zertifiziert:"
                    label-for="cert">
        <b-form-select id="cert"
                       :options="certs"
                       required
                       v-model="form.certified">
        </b-form-select>
      </b-form-group>

      <b-form-group id="GeneralLogo"
                    label="Logo-Name:"
                    label-for="genLogo">
        <b-form-input id="genLogo"
                      type="text"
                      v-model="form.logo"
                      required>
        </b-form-input>
      </b-form-group>



      <b-button class="small" style="float: right; margin-left: 0.5vw;" @click = "deleteTag">-</b-button>
      <b-button class="small" style="float: right;"  @click = "addTag">+</b-button>


      <br>
      <div v-for= "(tag,index) in form.tags">
       <!-- <b-form> -->
        <b-form-group id="'GeneralTag' + index"
                      label="Tag:"
                      label-for="'genTag' + index">
          <b-form-input id="'genTag' + index"
                        type="text"
                        v-model="form.tags[index]"
                        required>
          </b-form-input>
        </b-form-group>

      </div>

     <br>

    <b-button class="small" style="float:left; margin-right: 0.5vw;" @click = "addInputFormat">+ Input</b-button>
    <b-button class="small" style="float:left;" @click = "deleteInputFormat">- Input</b-button>
    <b-button class= "small" style="float:right; margin-left: 0.5vw;" @click = "deleteOutputFormat">- Output</b-button>
    <b-button class= "small" style="float:right;" @click = "addOutputFormat">+ Output</b-button>

    <br><br>

    <div>
      <div  class = "left" style.overflow= "hidden">
        <div v-for="(inputFormat, index) in form.formatIn" class = "lefter">
          <!-- <b-form> -->
          <h5>{{index + 1}}. Input Format</h5>
          <b-form-group id="'InpFormatType' + index"
                        label="Typ:"
                        label-for="'InputType'+ index">
            <b-form-input id="'InputType'+ index"
                          type="text"
                          v-model="inputFormat.type"
                          required>
            </b-form-input>
          </b-form-group>

          <b-form-group id="'InpFormatVersion' + index"
                        label="Version:"
                        label-for="'InputVersion'+ index">
            <b-form-input id="'InputVersion'+ index"
                          type="text"
                          v-model="inputFormat.version"
                          required>
            </b-form-input>
          </b-form-group>

          <b-form-group id="'InpComp' + index"
                        label="Kompatibilität:"
                        label-for="'InputComp'+ index">
            <b-form-select id="'InputComp'+ index"
                           :options="comps"
                           required
                           v-model="inputFormat.compatibilityDegree">
            </b-form-select>
          </b-form-group>

        </div>
      </div>

      <div class = "right">
        <div  v-for="(outputFormat, index) in form.formatOut" class= "righter">

          <h5>{{index + 1}}. Output Format</h5>
          <b-form-group id="'OutFormatType' + index"
                        label="Typ:"
                        label-for="'OutputType' + index">
            <b-form-input id="'OutputType' + index"
                          type="text"
                          v-model="outputFormat.type"
                          required>
            </b-form-input>
          </b-form-group>

          <b-form-group id="'OutFormatVersion' + index"
                        label="Version:"
                        label-for="'OutputVersion'+ index">
            <b-form-input id="'OutputVersion'+ index"
                          type="text"
                          v-model="outputFormat.version"
                          required>
            </b-form-input>
          </b-form-group>

          <b-form-group id="'OutComp'+ index"
                        label="Kompatibilität:"
                        label-for="'OutputComp'+ index">
            <b-form-select id="'OutputComp'+ index"+ index
                           :options="comps"
                           required
                           v-model="outputFormat.compatibilityDegree">
            </b-form-select>
          </b-form-group>

      </div>
    </div>
 </div>
</b-form>
</div>
</template>

<script>
export default {

  created() {
    if(!this.pedit) {
      this.deleteInputFormat();
      this.deleteOutputFormat();
    }
    },

  watch: {
    pform: function () {
      this.form = JSON.parse(JSON.stringify(this.pform))
      }
    },

  props: {
    pedit:{
      type: Boolean,
      default: false,
    },
    pform: {
      default() {
        return {
        id: 0,
        name: "",
        certified: "false",
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

    }
  }
  }
  },

  data () {
    return {
       form: JSON.parse(JSON.stringify(this.pform)),
        comps: [
        { text: 'Wählen Sie aus', value: "" },
        "strikt", "flexibel"
      ],

      certs: [{ text: 'Ja', value: "true" }, { text: 'Nein', value: "false" }],
      show: true
    }
  },
  methods: {
    onSubmit (evt) {
      evt.preventDefault();

      try{
        if(this.form.formatIn.length == 0 && this.form.formatOut.length == 0) {throw "Bitte geben Sie mindestens ein Ein- oder Ausgabeformat an."}

        if(this.pedit) {

        alert(JSON.stringify(this.form));
        this.axios.put('/services', JSON.stringify(this.form))
                 .then(function (response) { alert(response);})
                 .catch(function (error) {alert(error);});

        } else {
        this.form.date = Date.now();
        alert(this.form.date);


        alert(JSON.stringify(this.form));

        this.axios.post('/services', JSON.stringify(this.form))
                 .then(function (response) { alert(response);})
                 .catch(function (error) {alert(error);});
        }

      } catch(err) {
        alert(err);
      }

    },
    onReset (evt) {
      evt.preventDefault();
      /* Reset our form values */
      if(this.pedit){
       this.form = JSON.parse(JSON.stringify(this.pform));

      } else {
            this.form.organisation = ""
            this.form.name = "";
            this.form.certified = "";
            this.form.version = "";
            this.form.logo = "";
            this.form.tags = [""];
            this.form.formatIn = [  {
                type: "",
                version: "",
                compatibilityDegree: ""
              }];
            this.form.formatOut= [  {
                type: "",
                version: "",
                compatibilityDegree: ""
              }];
              this.deleteInputFormat();
              this.deleteOutputFormat();


      }
      /* Trick to reset/clear native browser form validation state */
      this.show = false;
      this.$nextTick(() => { this.show = true });
    },

    addInputFormat () {
      this.form.formatIn.push(  {
          type: "",
          version: "",
          compatibilityDegree: ""
        })
    },

    deleteInputFormat () {
      this.form.formatIn.pop();
    },

    addOutputFormat () {
      this.form.formatOut.push(  {
          type: "",
          version: "",
          compatibilityDegree: ""
        })
    },

    deleteOutputFormat () {
      this.form.formatOut.pop();
    },

    addTag () {
      this.form.tags.push("")
    },

    deleteTag () {
      if(this.form.tags.length > 1) {
        this.form.tags.pop();
      } else {
        alert("Bitte geben Sie mindestens einen Tag an.")
      }

    }

  }
}
</script>

<style>

.right{
  float: right;
  width: 45%;
}

.left{
  float: left;
  width: 45%;
}

.lefter{
  float: left;
  width: 90%;
  padding: 3%;
}

.righter {
  float: right;
  width: 90%;
  padding: 3%;
}

.small{
  size: small;
  block: true;

}



</style>

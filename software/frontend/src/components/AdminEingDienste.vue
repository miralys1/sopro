<template>
  <div class = "out">
    <b-form @submit="onSubmit" @reset="onReset" v-if="show">
      <b-button type="submit" variant="primary" style="float:right;">Submit</b-button>
      <b-button type="reset" variant="danger" style="float:right;">Reset</b-button>
      <h4>General Info</h4>
      <b-form-group id="GeneralName"
                    label="Name:"
                    label-for="genName">
        <b-form-input id="genName"
                      type="text"
                      v-model="form.name"
                      required
                      placeholder="Enter a name">
        </b-form-input>
      </b-form-group>

      <b-form-group id="GeneralOrg"
                    label="Organisation:"
                    label-for="genOrg">
        <b-form-input id="genOrg"
                      type="text"
                      v-model="form.organisation"
                      required
                      placeholder="Enter an organisation">
        </b-form-input>
      </b-form-group>

      <b-form-group id="GeneralVersion"
                    label="Version:"
                    label-for="genVersion">
        <b-form-input id="genVersion"
                      type="text"
                      v-model="form.version"
                      required
                      placeholder="Enter a version">
        </b-form-input>
      </b-form-group>

      <b-form-group id="GeneralLogo"
                    label="Logo:"
                    label-for="genLogo">
        <b-form-input id="genLogo"
                      type="text"
                      v-model="form.logo"
                      required
                      placeholder="Enter a logo name">
        </b-form-input>
      </b-form-group>

      <b-button class="small" style= "float: right;" @click = "deleteTag">-</b-button>
      <b-button class="small" style= "float: right;" @click = "addTag">+</b-button>

      <br>
      <div v-for= "(tag,index) in form.tags">
       <!-- <b-form> -->
        <b-form-group id="GeneralTag"
                      label="Tag:"
                      label-for="genTag">
          <b-form-input id="genTag"
                        type="text"
                        v-model="form.tags[index]"
                        required
                        placeholder="Enter a tag">
          </b-form-input>
        </b-form-group>
      <!-- </b-form> -->
      </div>
    <!-- </b-form> -->


    <b-button class="small" style="float:left;" @click = "addInputFormat">Add In Format</b-button>
    <b-button class="small" style="float:left;" @click = "deleteInputFormat">Delete In Format</b-button>
    <b-button class= "small" style="float:right;" @click = "deleteOutputFormat">Delete Out Format</b-button>
    <b-button class= "small" style="float:right;" @click = "addOutputFormat">Add Out Format</b-button>

    <br><br>

    <div>
      <div  class = "left" style.overflow= "hidden">
        <div v-for="(inputFormat, index) in form.inputFormats" class = "lefter">
          <!-- <b-form> -->
          <h3>{{index + 1}}. Input Format</h3>
          <b-form-group id="InpFormatType"
                        label="Type:"
                        label-for="InputType">
            <b-form-input id="InputType"
                          type="text"
                          v-model="inputFormat.type"
                          required
                          placeholder="Enter a type">
            </b-form-input>
          </b-form-group>

          <b-form-group id="InpFormatVersion"
                        label="Version:"
                        label-for="InputVersion">
            <b-form-input id="InputVersion"
                          type="text"
                          v-model="inputFormat.version"
                          required
                          placeholder="Enter a version">
            </b-form-input>
          </b-form-group>

          <b-form-group id="InpComp"
                        label="Compatibility Degree:"
                        label-for="InputComp">
            <b-form-select id="InputComp"
                           :options="comps"
                           required
                           v-model="inputFormat.compatibilityDegree">
            </b-form-select>
          </b-form-group>
          <!-- </b-form> -->
        </div>
      </div>

      <div class = "right">
        <div  v-for="(outputFormat, index) in form.outputFormats" class= "lefter">
        <!-- <b-form> -->
          <h3>{{index + 1}}. Output Format</h3>
          <b-form-group id="OutFormatType"
                        label="Type:"
                        label-for="OutputType">
            <b-form-input id="OutputType"
                          type="text"
                          v-model="outputFormat.type"
                          required
                          placeholder="Enter a type">
            </b-form-input>
          </b-form-group>

          <b-form-group id="OutFormatVersion"
                        label="Version:"
                        label-for="OutputVersion">
            <b-form-input id="OutputVersion"
                          type="text"
                          v-model="outputFormat.version"
                          required
                          placeholder="Enter a version">
            </b-form-input>
          </b-form-group>

          <b-form-group id="OutComp"
                        label="Compatibility Degree:"
                        label-for="OutputComp">
            <b-form-select id="OutputComp"
                           :options="comps"
                           required
                           v-model="outputFormat.compatibilityDegree">
            </b-form-select>
          </b-form-group>
        <!-- </b-form> -->
      </div>
    </div>
 </div>
</b-form>
</div>
</template>

<script>
export default {
  created() {
      this.deleteInputFormat();
      this.deleteOutputFormat();
    },

  data () {
    return {
      form: {
        name: "",
        organisation: "",
        version: "",
        date: 0,
        logo: "",
        tags: [""
        ],
      inputFormats: [
        {
          type: "",
          version: "",
          compatibilityDegree: ""
        }
      ],
      outputFormats: [
        {
          type: "",
          version: "",
          compatibilityDegree: ""
        }
      ],
    },
      comps: [
        { text: 'Select One', value: "" },
        "strict", "flexible"
      ],
      show: true
    }
  },
  methods: {
    onSubmit (evt) {
      try{
        if(this.form.inputFormats.length == 0 && this.form.outputFormats.length == 0) {throw "You need to enter an input or output format"}
        this.form.date = Date.now();
        evt.preventDefault();

        this.axios.post('/services', this.form)
                 .then(function (response) { alert(response);})
                 .catch(function (error) {alert(error);});

      } catch(err) {
        alert(err);
      }

    },
    onReset (evt) {
      evt.preventDefault();
      /* Reset our form values */
      this.form.organisation = ""
      this.form.name = "";
      this.form.version = "";
      this.form.logo = "";
      this.form.tags = [""];
      this.form.inputFormats = [  {
          type: "",
          version: "",
          compatibilityDegree: ""
        }];
      this.form.outputFormats= [  {
          type: "",
          version: "",
          compatibilityDegree: ""
        }];
        this.deleteInputFormat();
        this.deleteOutputFormat();
      /* Trick to reset/clear native browser form validation state */
      this.show = false;
      this.$nextTick(() => { this.show = true });
    },

    addInputFormat () {
      this.form.inputFormats.push(  {
          type: "",
          version: "",
          compatibilityDegree: ""
        })
    },

    deleteInputFormat () {
      this.form.inputFormats.pop();
    },

    addOutputFormat () {
      this.form.outputFormats.push(  {
          type: "",
          version: "",
          compatibilityDegree: ""
        })
    },

    deleteOutputFormat () {
      this.form.outputFormats.pop();
    },

    addTag () {
      this.form.tags.push("")
    },

    deleteTag () {
      this.form.tags.pop();
    }

  }
}
</script>

<style>

.right{
  float: right;
  width: 45%;
  padding: 20px;
}

.left{
  float: left;
  width: 45%;
  padding: 20px;
}

.lefter{
  float: left;
  width: 50%;
  padding: 20px;
  border: dashed;
  border-radius: 25px;
}

.small{
  size: small;
  block: true;

}



</style>

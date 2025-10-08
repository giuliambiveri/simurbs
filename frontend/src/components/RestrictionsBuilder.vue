<template>
  <div :class="['mt-5 md:mt-0 builder-box p-[10px] rounded-lg h-fit aspect-square bg-surface-200 dark:bg-surface-900', gridSizeClass]">
    <div v-for="y in yRange" class="flex builder-row" :key="y">
      <div v-for="x in xRange" class="flex builder-cell border-[1px] border-surface-200 dark:border-surface-900" :key="x">
        <Icon
            :icon="
            !getBlock(-x, y) || !getBlock(-x, y).blockType ? 'material-symbols:question-mark-rounded' :
            getBlock(-x, y).isModified ? 'material-symbols:checkbook-rounded' :
            getBlock(-x, y).blockType === 'RESIDENTIALBLOCK' ? 'material-symbols:home-rounded' :
            getBlock(-x, y).blockType === 'PARKBLOCK' ? 'material-symbols:park-rounded' :
            getBlock(-x, y).blockType === 'WATERBLOCK' ? 'material-symbols:water-rounded' :
            'material-symbols:error-rounded'"
            :class="[
            'block-cell h-auto xs:w-auto',
            { 'selected-cell': isSelected(-x, y) },
            { 'zone-restricted-cell': isZoneRestricted(-x, y) },
            ... !getBlock(-x, y) || !getBlock(-x, y).blockType ? [isSelected(-x, y) ? 'bg-gray-600' : 'bg-gray-400', 'hover:bg-gray-500', 'text-gray-950'] :
            getBlock(-x, y).blockTypeRestriction !== getBlock(-x, y).blockType && getBlock(-x, y).blockTypeRestriction !== 'NONE' ? getRestrictionColorClass(getBlock(-x, y).blockTypeRestriction, isSelected(-x, y)) :
            getBlock(-x, y).blockType === 'RESIDENTIALBLOCK' ? [isSelected(-x, y) ? 'bg-orange-600' : 'bg-orange-400', 'hover:bg-orange-500', 'text-orange-950'] :
            getBlock(-x, y).blockType === 'PARKBLOCK' ? [isSelected(-x, y) ? 'bg-green-600' : 'bg-green-400', 'hover:bg-green-500', 'text-green-950'] :
            getBlock(-x, y).blockType === 'WATERBLOCK' ? [isSelected(-x, y) ? 'bg-blue-600' : 'bg-blue-400', 'hover:bg-blue-500', 'text-blue-950'] :
            [isSelected(-x, y) ? 'bg-red-600' : 'bg-red-400', 'hover:bg-red-500', 'text-red-950']
            ]"
            @click="openBlockDialog(-x, y)"
        />
      </div>
    </div>
  </div>

  <div class="card flex justify-center">
    <Dialog v-model:visible="visible" modal :header="`Edit block (${activeBlock.x}, ${activeBlock.y})`" :style="{ width: '50%', maxWidth: '700px' }" @hide="this.CBD = false">
      <span v-if="!this.CBD" class="text-surface-600 dark:text-surface-0/70 block mb-5">The selected block is already a <b>{{
          this.capitalizeFirstLetter(selectedBlockType)
        }} </b>
      </span>
      <span v-else class="text-surface-600 dark:text-surface-0/70 block mb-5"> <b class=" text-yellow-500 dark:text-yellow-300">The selected block is the CBD and can only be a RESIDENTIAL BLOCK </b>
      </span>
      <span v-if="!this.CBD" class="text-surface-600 dark:text-surface-0/70 block mb-5 font-semibold">Select a Block type restriction:
      </span>
      <div v-if="!this.CBD" :class="isZoneRestrictionFree(activeBlock.x, activeBlock.y) ? 'grid grid-cols-4' : 'grid grid-cols-3'">
        <div v-for="T in test2" v-if="((selectedBlockType==='RESIDENTIALBLOCK')&&(this.isZoneRestrictionFree(activeBlock.x, activeBlock.y)))" class="flex flex-col justify-center items-center">
          <input type="radio" name="blockTypeRestriction" v-bind:id="T[0]"
                 hidden v-model="blockTypeRestriction" v-bind:value="T[1]"/>

          <label v-bind:for="T[0]" v-bind:class="T[2] +' w-10 h-10 cursor-pointer rounded-lg'">
            <Icon v-bind:icon="'material-symbols:'+T[4]" class="w-10 h-10" color="black"/>
          </label>

          <label v-bind:for="T[0]" class="cursor-pointer mt-2">
            {{ T[3] }}
          </label>
        </div>
        <div v-for="T in test2.slice(1)" v-else class="flex flex-col justify-center items-center">
          <input type="radio" name="blockTypeRestriction" v-bind:id="T[0]"
                 hidden v-model="blockTypeRestriction" v-bind:value="T[1]"/>

          <label v-bind:for="T[0]" v-bind:class="T[2] +' w-10 h-10 cursor-pointer rounded-lg'">
            <Icon v-bind:icon="'material-symbols:'+T[4]" class="w-10 h-10" color="black"/>
          </label>

          <label v-bind:for="T[0]" class="cursor-pointer mt-2">
            {{ T[3] }}
          </label>
        </div>
      </div>

      <Divider/>

      <div
          v-for="T in test" v-if="(selectedBlockType === 'RESIDENTIALBLOCK' && blockTypeRestriction === 'NONE') || blockTypeRestriction === 'RESIDENTIALBLOCK'"
          class="gap-3 mb-3 items-center grid grid-cols-2">
        <label v-bind:for="T[1]" class="font-semibold">{{ T[0] }} Cost Limit:</label>
        <InputNumber v-bind:v-model="T[1]"
                     v-bind:placeholder="'Set '+T[0]+' cost limit'"
                     v-bind:inputId="T[1]"
                     :maxFractionDigits="10"
                     @focusout="checkForZero(true)"/>
      </div>

      <div class="flex justify-end gap-2">
        <Button type="button" label="Reset To Original" severity="secondary"
                @click="undoRestrictions(activeBlock, block);"></Button>
        <Button type="button" label="Save" @click="addBlock(activeBlock, block);"></Button>
      </div>
    </Dialog>
  </div>
</template>

<script>
import {Icon} from "@iconify/vue";
import Button from "primevue/button";
import Dialog from "primevue/dialog";
import InputText from "primevue/inputtext";
import InputNumber from "primevue/inputnumber";
import Divider from "primevue/divider";
import {useToast} from "primevue/usetoast";


export default {
  name: "RestrictionsBuilder",
  components: {Icon, Button, InputText, Dialog, InputNumber, Divider},
  props: {
    blocks: new Map(),
    grid_size: 0,
  },
  data() {
    return {
      test:[],
      test2:[],
      block: {
        blockType: null,
        blockTypeRestriction: null,
        localLimitTransportationCost: null,
        localLimitConstructionCost: null,
        localLimitRentCost: null,
        isModified: false
      },
      activeBlock: {x: null, y: null},
      CBD: false,
      visible: false,
      originalBlocks: new Map(),
      isOriginalCopied: false,
      toast: useToast()
    };
  },
  mounted(){
    this.CBD = false;
    this.test[0]=["Transportation","transportationCostLimit"]
    this.test[1]=["Construction","constructionCostLimit"]
    this.test[2]=["Rent","rentCostLimit"]
    this.test2[0]=["residentialRadio","RESIDENTIALBLOCK","bg-orange-400","Residential","home-rounded"]
    this.test2[1]=["parkRadio","PARKBLOCK","bg-green-400","Park","park-rounded"]
    this.test2[2]=["waterRadio","WATERBLOCK","bg-blue-400","Water","water-rounded"]
    this.test2[3]=["noneRadio","NONE","bg-gray-400","None","close-rounded"]
  },
  watch: {
    blocks: {
      handler(newVal) {
        if (!this.isOriginalCopied && newVal.size > 0) {
          this.originalBlocks = new Map(
              JSON.parse(JSON.stringify(Array.from(newVal)))
          );
          this.isOriginalCopied = true;
        }
      },
      immediate: true,
      deep: true
    }
  },
  computed: {
    gridSizeClass() {
      if (this.grid_size > 20) {
        return 'map-xl';
      }if (this.grid_size > 10) {
        return 'map-lg';
      }if (this.grid_size > 5) {
        return 'map-md';
      }if (this.grid_size > 1) {
        return 'map-sm';
      }else {
        return 'map-xs';
      }
    },
    xRange() {
      return Array.from({length: this.grid_size}, (_, i) => (i - (this.grid_size - 1) / 2));
    },
    yRange() {
      return Array.from({length: this.grid_size}, (_, i) => ((this.grid_size - 1) / 2) - i);
    },
    transportationCostLimit: {
      get() {
        return this.block.localLimitTransportationCost ? this.block.localLimitTransportationCost.value : this.block.localLimitTransportationCost;
      },
      set(value) {
        if (this.block.localLimitTransportationCost) {
          this.block.localLimitTransportationCost.value = value;
        }
      }
    },
    constructionCostLimit: {
      get() {
        return this.block.localLimitConstructionCost ? this.block.localLimitConstructionCost.value : this.block.localLimitConstructionCost;
      },
      set(value) {
        if (this.block.localLimitConstructionCost) {
          this.block.localLimitConstructionCost.value = value;
        }
      }
    },
    rentCostLimit: {
      get() {
        return this.block.localLimitRentCost ? this.block.localLimitRentCost.value : this.block.localLimitRentCost;
      },
      set(value) {
        if (this.block.localLimitRentCost) {
          this.block.localLimitRentCost.value = value;
        }
      }
    },
    blockTypeRestriction: {
      get() {
        return this.block.blockTypeRestriction;
      },
      set(value) {
        this.block.blockTypeRestriction = value;
      }
    },
    selectedBlockType: {
      get() {
        return this.block.blockType;
      }
    }
  },
  methods: {
    getRestrictionColorClass(restriction, isSelected) {
      if (!restriction) return [];
      return restriction === 'RESIDENTIALBLOCK' ? [isSelected ? 'bg-orange-600' : 'bg-orange-400', 'hover:bg-orange-500', 'text-orange-950'] :
          restriction === 'PARKBLOCK' ? [isSelected ? 'bg-green-600' : 'bg-green-400', 'hover:bg-green-500', 'text-green-950'] :
              restriction === 'WATERBLOCK' ? [isSelected ? 'bg-blue-600' : 'bg-blue-400', 'hover:bg-blue-500', 'text-blue-950'] :
                  [isSelected ? 'bg-red-600' : 'bg-red-400', 'hover:bg-red-500', 'text-red-950'];
    },
    capitalizeFirstLetter(text) {
      return text.charAt(0).toUpperCase() + text.slice(1).toLowerCase();
    },
    checkForZero(flag) {
      let foundZero = false

      if (this.transportationCostLimit === 0 || this.rentCostLimit === 0 || this.constructionCostLimit === 0) {
        foundZero=true
        if (flag) {
          this.toast.add({
            severity: 'error',
            summary: 'Invalid value',
            detail: 'Giving 0 as a value is not allowed',
            life: 3000
          });
        }
      }

      if (this.transportationCostLimit === 0) {
        this.transportationCostLimit = null;
      }

      if (this.rentCostLimit === 0) {
        this.rentCostLimit = null;
      }

      if (this.constructionCostLimit === 0) {
        this.constructionCostLimit = null;
      }

      return foundZero
    },
    getBlock(x, y) {
      let block = this.blocks.get(`Coordinates[x=${x}, y=${y}]`) || {
        blockType: null,
        blockTypeRestriction: null,
        localLimitTransportationCost: null,
        constructionCost: null,
        rentPrice: null
      };
      return JSON.parse(JSON.stringify(block));
    },
    checkResidentialBlock(block){
      if (block.blockType === "RESIDENTIALBLOCK") {
          if (!block.localLimitTransportationCost.value) block.localLimitTransportationCost.value = 0;
          if (!block.localLimitConstructionCost.value) block.localLimitConstructionCost.value = 0;
          if (!block.localLimitRentCost.value) block.localLimitRentCost.value = 0;
        }
    },
    addBlock(coordinates, block) {
      if (!this.checkForZero(true)) {
        this.checkResidentialBlock(block);
        this.block.isModified = true;
        this.blocks.set(`Coordinates[x=${coordinates.x}, y=${coordinates.y}]`, block)
        this.visible = false
      }
    },
    undoRestrictions(coordinates, block) {
      const originalBlockKey = `Coordinates[x=${coordinates.x}, y=${coordinates.y}]`;
      if (this.originalBlocks.has(originalBlockKey)) {
        const originalBlock = this.originalBlocks.get(originalBlockKey);
        Object.assign(block, JSON.parse(JSON.stringify(originalBlock)));
        this.block.isModified = false;
        this.checkResidentialBlock(block);
        this.blocks.set(originalBlockKey, block);
        this.openBlockDialog(coordinates.x, coordinates.y)
      } else {
        this.toast.add({
          severity: 'warn',
          summary: 'Reset Failed',
          detail: 'No original block found to undo.',
          life: 3000
        });
      }
    },
    openBlockDialog(x, y) {
      if(x === 0 && y === 0){
        this.CBD = true;
      }
      this.visible = true;
      this.activeBlock = {x, y}
      this.block = this.getBlock(x, y) || {
        blockType: null,
        blockTypeRestriction: null,
        localLimitTransportationCost: null,
        localLimitRentCost: null,
        localLimitConstructionCost: null
      }
      this.checkForZero(false);
    },
    isSelected(x, y) {
      return this.activeBlock && this.activeBlock.x === x && this.activeBlock.y === y;
    },
    isZoneRestricted(x, y) {
      return y < Math.abs(x) - (this.grid_size - 1) / 2 || y > -Math.abs(x) + (this.grid_size - 1) / 2;
    },
    isZoneRestrictionFree(x, y) {
      return !this.isZoneRestricted(x,y);
    },
  }
}
</script>

<style scoped>

.builder-row {
  display: flex;
  width: 100%;
  max-width: 100%;
  overflow: hidden;
  box-sizing: border-box;
}

.builder-cell {
  flex: 1 1 0;
  min-width: 0;
  box-sizing: border-box;
  text-align: center;
  cursor: pointer;
  aspect-ratio: 1;
  overflow: hidden;
}

.block-cell {
  width: 100%;
}

input[name="blockTypeRestriction"]:checked ~ label {
  font-weight: bold;
}

input[name="blockTypeRestriction"]:checked + label {
  transform: scale(1.5);
}

.map-xs {
  width: 100px;
}


.map-sm {
  width: 300px;
}

.map-md {
  width: 450px;
}

.map-lg {
  width: 545px;
}

.map-xl {
  --navbar-height: 70px;
  --footer-height: 50px;
  width: calc(100vh - var(--navbar-height) - var(--footer-height));
}
</style>
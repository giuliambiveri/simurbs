<template>
  <div class="absolute top-[20px] right-[190px]">
    <Button @click="openPosition(isOld ? 'left' : 'right')" severity="warning" >
      {{(isOld && $store.state.isOldBlockInfoBoxOpen) || (!isOld && $store.state.isBlockInfoBoxOpen) ? 'Close Block Info' : 'Open Block Info' }}
    </Button>
  </div>

  <Dialog v-model:visible="visible" :header="`Block (${blockInfo.position.x}, ${blockInfo.position.z})`"
          :style="{ width: '25rem' }" :position="position" :draggable="true">
    <!-- For subheader of the block info uncomment and edit the tag below -->
    <!--    <span class="text-surface-600 dark:text-surface-0/70 block mb-5"><<PUT SUBHEADER HERE>></span>-->
    <div>
      <strong>Manhattan distance: </strong>
      <span class="value"> {{ getDistance(blockInfo.position.x, blockInfo.position.z) }} Km</span>
    </div>

    <Divider type="solid" />

    <div>
      <strong>Block type: </strong>
      <span class="value"> {{ blockInfo.blockType.toLowerCase() }}</span>
    </div>

    <Divider type="solid" />

    <template v-if="blockInfo.blockType === 'RESIDENTIALBLOCK'">
      <div>
        <strong>Building Details:</strong>
        <div>
          <h5><strong>Height:</strong> <span class="value">{{ formatNumber(blockInfo.building.height) }} floors</span></h5>
          <h5><strong>People:</strong> <span class="value">{{ formatNumber(blockInfo.building.people) }}</span></h5>
          <h5><strong>Building type:</strong> <span class="value">{{ blockInfo.building.buildingType.toLowerCase() }}</span></h5>
        </div>
      </div>

      <template v-for="(item, index) in items" :key="index">
        <Divider type="solid"/>
        <div>
          <strong>{{ item.label }}:</strong> <span class="value"> {{ displayMoney(item.value, item.decimals) }}</span>
        </div>
      </template>
    </template>

    <Button @click="handleCompare" severity="info" v-if="isSplitView" class="mt-3">
      Compare with the {{ hasOldSimulation ? 'old' : 'current' }} version
    </Button>
  </Dialog>
</template>


<script>
import {useToast} from "primevue/usetoast";
import Dialog from "primevue/dialog";
import InputText from "primevue/inputtext";
import Button from "primevue/button";
import Divider from "primevue/divider";

export default {
  name: "BlockInfoBox",
  components: {
    Dialog,
    InputText,
    Button,
    Divider
  },
  data() {
    return {
      isOpen: false,
      isComparisonOpen: false,
      toast: useToast(),
      position: 'center',
      items: [
        { label: 'Rent cost', value: this.blockInfo.rentCost, decimals: 3 },
        { label: 'Local Rent Cost Limit', value: this.blockInfo.localLimitRentCost, decimals: 3 },
        { label: 'Land Price', value: this.blockInfo.landPrice, decimals: 2 },
        { label: 'Construction Cost', value: this.blockInfo.constructionCost, decimals: 0 },
        { label: 'Local Construction Cost Limit', value: this.blockInfo.localLimitConstructionCost, decimals: 0 },
        { label: 'Transportation Cost', value: this.blockInfo.transportationCost, decimals: 1 },
        { label: 'Local Transportation Cost Limit', value: this.blockInfo.localLimitTransportationCost, decimals: 1 },
      ],
    }
  },
  watch: {
    comparisonBlockData(newVal) {
      this.$emit("update-comparison-block-info", newVal);
    },
    isSplitView(newVal) {
      this.isSplitView = newVal;
    }
  },
  props: {
    blockInfo: null,
    hasOldSimulation: Boolean,
    comparisonBlockData: null,
    isSplitView: Boolean,
    isOld: Boolean
  },
  computed: {
    visible: {
      get() {
        return (this.isOld && this.$store.state.isOldBlockInfoBoxOpen) || (!this.isOld && this.$store.state.isBlockInfoBoxOpen)
      },
      set(value) {
        if (this.isOld) {
          this.$store.state.isOldBlockInfoBoxOpen = value
        } else {
          this.$store.state.isBlockInfoBoxOpen = value
        }
      }
    }
  },
  mounted() {
    console.log("block info", this.blockInfo)
  },
  methods: {
    openPosition(position) {
      this.position = position;
      if (this.isOld) {
        if (this.$store.state.isOldCityInfoBoxOpen) {
          this.$store.state.isOldCityInfoBoxOpen = false;
        }
        this.$store.state.isOldBlockInfoBoxOpen = !this.$store.state.isOldBlockInfoBoxOpen;
      } else {
        if (this.$store.state.isCityInfoBoxOpen) {
          this.$store.state.isCityInfoBoxOpen = false;
        }
        this.$store.state.isBlockInfoBoxOpen = !this.$store.state.isBlockInfoBoxOpen;
      }
    },
    getDistance(x, z) {
      return Math.abs(x) + Math.abs(z);
    },
    toggleBlockInfoBox() {
      this.isOpen = !this.isOpen;
      if (!this.isOpen) {
        this.$emit('close-comparison');
      }
      console.log(this.blockInfo);
    },
    formatNumber(value) {
      return Number(value).toLocaleString();
    },
    formatDouble(num) {
      return num.toLocaleString('en-US', {minimumFractionDigits: 2, maximumFractionDigits: 2});
    },
    displayMoney(param, other) {
      let added;
      switch (other) {
        case 1:
          added = "/Km per Month";
          break;
        case 2:
          added = "/m\u00B2";
          break;
        case 3:
          added = "/m\u00B2";
          break;
        default:
          added = "";
      }
      return param.value === 0.0 ? 'None' : this.formatDouble(param.value) + " " + param.currency + added;
    },
    handleCompare() {
      console.log("old sim", this.comparisonBlockData)
      console.log("new sim", this.blockInfo)
      if (this.isOld) {
        this.$emit('show-comparison', this.comparisonBlockData, this.blockInfo);
      } else {
        this.$emit('show-comparison', this.blockInfo, this.comparisonBlockData);
      }
    }
  }
}
</script>

<style scoped>

.value {
  font-weight: normal;
}

div[role='dialog'] > div:nth-child(2) > div {
  display: grid;
  grid-template-columns: 1fr 1fr;
  align-items: center;
}

div[role='separator'] {
  margin: 10px 0;
}

</style>

<template>
  <div class="absolute top-[20px] right-[20px]">
    <Button @click="openPosition(isOld ? 'left' : 'right')" severity="help" >
      {{ (isOld && $store.state.isOldCityInfoBoxOpen) || (!isOld && $store.state.isCityInfoBoxOpen) ? 'Close City Info' : 'Open City Info' }}
    </Button>
  </div>

  <Dialog v-model:visible="visible" :header="`${simInfo.name}`" :style="{ width: '25rem' }" :position="position"
          :draggable="true">
    <div>
      <strong>Average Income:</strong>
      <span class="value">{{ formatDouble(simInfo.averageIncome.value) }} {{ simInfo.averageIncome.currency }}</span>
    </div>

    <Divider type="solid" />

    <div>
      <strong>Commuter Costs:</strong>
      <span class="value">{{ formatDouble(simInfo.commuterCosts.value) }} {{ simInfo.commuterCosts.currency }}/Km per Month</span>
    </div>

    <Divider type="solid" />

    <div>
      <strong>Population:</strong>
      <span class="value">{{ formatNumber(cityData.population) }}</span>
    </div>

    <Divider type="solid" />

    <div>
      <strong>Total blocks:</strong>
      <span class="value">{{ formatNumber(cityData.blockCounter) }}</span>
    </div>

    <Divider type="solid" />

    <div>
      <strong>Global limit on construction cost:</strong>
      <span class="value">{{ displayLimit(cityData.globalMaxConstructionCost) }}</span>
    </div>

    <Divider type="solid" />

    <div>
      <strong>Global limit on rent cost:</strong>
      <span class="value">{{ displayLimit(cityData.globalMaxRentCost) }}</span>
    </div>
  </Dialog>
</template>

<script>
import Dialog from "primevue/dialog";
import InputText from "primevue/inputtext";
import Button from "primevue/button";
import Divider from "primevue/divider";

export default {
  name: "CityInfoBox",
  components: {
    Dialog,
    InputText,
    Button,
    Divider
  },
  emits: ['clicked'],
  data() {
    return {
      isOpen: false,
      position: 'center'
    }
  },
  props: {
    simInfo: Object,
    cityData: Object,
    isOld: Boolean
  },
  computed: {
    visible: {
      get() {
        return (this.isOld && this.$store.state.isOldCityInfoBoxOpen) || (!this.isOld && this.$store.state.isCityInfoBoxOpen)
      },
      set(value) {
        if (this.isOld) {
          this.$store.state.isOldCityInfoBoxOpen = value
        } else {
          this.$store.state.isCityInfoBoxOpen = value
        }
      }
    }
  },
  methods: {
    openPosition(position) {
      this.position = position;
      if (this.isOld) {
        if (this.$store.state.isOldBlockInfoBoxOpen) {
          this.$store.state.isOldBlockInfoBoxOpen = false;
        }
        this.$store.state.isOldCityInfoBoxOpen = !this.$store.state.isOldCityInfoBoxOpen;
      } else {
        if (this.$store.state.isBlockInfoBoxOpen) {
          this.$store.state.isBlockInfoBoxOpen = false;
        }
        this.$store.state.isCityInfoBoxOpen = !this.$store.state.isCityInfoBoxOpen;
      }
    },
    toggleCityInfoBox() {
      this.isOpen = !this.isOpen;
    },
    formatNumber(value) {
      return Number(value).toLocaleString();
    },
    formatDouble(num) {
      return num.toLocaleString('en-US', {minimumFractionDigits: 2, maximumFractionDigits: 2});
    },
    handleClick() {
      this.toggleCityInfoBox();
      this.$emit('clicked');
    },
    displayLimit(value) {
      return value.value === 0.0 ? 'None' : this.formatDouble(value.value) + " " + value.currency + "/Km\u00B2";
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

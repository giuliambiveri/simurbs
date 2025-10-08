<template>
  <Dialog v-model:visible="isComparePopupOpen" modal
          :header="`Compare blocks in position (${blockInfo ? blockInfo.coordinates.x : comparisonBlockInfo.coordinates.x}, ${blockInfo ? blockInfo.coordinates.z : comparisonBlockInfo.coordinates.z})`"
          :style="{ width: '80%', maxWidth: '900px' }">
    <div class="content p-5 text-left">
      <template v-if="comparisonBlockInfo && Object.keys(comparisonBlockInfo).length > 0 && blockInfo && Object.keys(blockInfo).length > 0">
        <table class="w-full">
          <thead>
          <tr>
            <th>Attribute</th>
            <th>Old version</th>
            <th>Current version</th>
            <th>Variation</th>
          </tr>
          </thead>
          <tbody>
          <tr>
            <td class="font-bold">Block type</td>
            <td>{{ comparisonBlockInfo.blockType }}</td>
            <td>{{ blockInfo.blockType }}</td>
            <td :style="{color: blockInfo.blockType != comparisonBlockInfo.blockType ? 'green' : ''}">
              {{ blockInfo.blockType != comparisonBlockInfo.blockType ? `Changed to ${blockInfo.blockType}` : 'None' }}
            </td>
          </tr>

          <template
              v-if="comparisonBlockInfo.blockType == 'RESIDENTIALBLOCK' || blockInfo.blockType == 'RESIDENTIALBLOCK'">
            <tr>
              <td class="font-bold">Building</td>
              <td v-for="key in ['comparisonBlockInfo', 'blockInfo']" :key="key">
                <template v-if="hasBuildingInfo(key)">
                  <div><b>Height: </b>{{ getBuildingInfo(key, 'height') }}</div>
                  <div><b>People: </b>{{ getBuildingInfo(key, 'people') }}</div>
                </template>
                <template v-else>
                  None
                </template>
              </td>
              <td>
                <div v-for="(value, key) in comparisonData" :key="key" :style="getVariationColor(value.current, value.comparison)">
                  <b>{{ key }}: </b>{{ getDifference(value.current, value.comparison) }}
                </div>
              </td>
            </tr>
            <tr v-for="(item, index) in tableItems" :key="index">
              <td class="font-bold">{{ item.label }}</td>
              <td>{{ displayMoney(comparisonBlockInfo[item.key], item.decimals) }}</td>
              <td>{{ displayMoney(blockInfo[item.key], item.decimals) }}</td>
              <td :style="getVariationColor(blockInfo[item.key].value, comparisonBlockInfo[item.key].value)">
                {{ displayVariation(blockInfo[item.key], comparisonBlockInfo[item.key], item.decimals) }}
              </td>
            </tr>
          </template>
          </tbody>
        </table>
      </template>
      <template v-else>
        <div class="header">
          <h1><strong>The block does not exist in the other version.</strong></h1>
        </div>
      </template>
    </div>
  </Dialog>

</template>

<script>
import Dialog from "primevue/dialog";

export default {
  name: "CompareInfoBox",
  components: {
    Dialog
  },
  props: {
    isOpen: Boolean,
    blockInfo: Object,
    comparisonBlockInfo: Object,
    isOld: Boolean
  },
  data() {
    return {
      isDragging: false,
      dragStartX: 0,
      dragStartY: 0,
      offsetX: 0,
      offsetY: 0,
      tableItems: [
        { label: 'Rent cost', key: 'rentCost', decimals: 3 },
        { label: 'Block rent cost limit', key: 'localLimitRentCost', decimals: 3 },
        { label: 'Land price', key: 'landPrice', decimals: 3 },
        { label: 'Construction cost', key: 'constructionCost', decimals: 0 },
        { label: 'Block construction cost limit', key: 'localLimitConstructionCost', decimals: 0 },
        { label: 'Transportation cost', key: 'transportationCost', decimals: 1 },
        { label: 'Block transportation cost limit', key: 'localLimitTransportationCost', decimals: 1 }
      ]
    };
  },
  computed: {
    getVariationColor() {
      return (first, second) => {
        return {
          color: first - second < 0 ? 'red' : first - second == 0 ? '' : 'green'
        };
      };
    },
    isComparePopupOpen: {
      get() {
        return this.isOpen
      },
      set(value) {
        if (value == false) {
          this.$emit('close')
        }
      }
    },
    comparisonData() {
      return {
        Height: {
          current: this.blockInfo.building.height,
          comparison: this.comparisonBlockInfo.building.height
        },
        People: {
          current: this.blockInfo.building.people,
          comparison: this.comparisonBlockInfo.building.people
        }
      };
    }
  },
  methods: {
    formatDouble(num) {
      return num.toLocaleString('en-US', {minimumFractionDigits: 2, maximumFractionDigits: 2});
    },
    getDisplayString(param, first, second, other) {
      let added;
      switch (other) {
        case 1:
          added = "/Km per Month";
          break;
        case 2:
          added = "/Km\u00B2";
          break;
        case 3:
          added = "/m\u00B2";
          break;
        default:
          added = "";
      }
      const value = first.value - (second ? second.value : 0);
      return value === 0.0 ? 'None' : this.formatDouble(value) + " " + first.currency + added;
    },
    displayMoney(param, other) {
      return this.getDisplayString(param, param, null, other);
    },
    displayVariation(first, second, other) {
      return this.getDisplayString(first, first, second, other);
    },
    getDifference(current, comparison) {
      const difference = current - comparison;
      return difference === 0 ? 'None' : difference;
    },
    hasBuildingInfo(key) {
      const building = this[key].building;
      return building.height !== 0 && building.people !== 0;
    },
    getBuildingInfo(key, attribute) {
      return this[key].building[attribute];
    }
  }
}
</script>

<style scoped>

.content {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
}

table, th, td {
  border: 1px solid rgb(var(--surface-300));
}

@media (prefers-color-scheme: dark) {
  table, th, td {
    border: 1px solid rgb(var(--surface-600));
  }
}

th, td {
  padding: 5px;
}

</style>

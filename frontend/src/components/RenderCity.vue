<template>
  <div class="city-simulation relative" style="min-height:100%;min-width:100%;">
    <h1 class=" text-white text-4xl font-bold name-box" >
      {{ simulationInfo.name }}
    </h1>


    <div v-if="!isCitySpecPopulated" class="loading-screen">
      <p>Loading map, please wait...</p>
    </div>
    <MetroBlocksScene v-if="isCitySpecPopulated"
                      :city-spec="this.citySpec"
                      @tile-select="handleTileSelection"
                      @tile-deselect="handleTileDeselection"/>

    <BlockInfoBox :style="{ transition: blockInfoBoxTransitionStyle }"
                  :class="{ 'shifted-down': isCityInfoBoxOpen}"
                  :hasOldSimulation="hasOldSimulation"
                  :isSplitView="isSplitView"
                  v-if="isBlockInfoBoxVisible"
                  :blockInfo="blockData"
                  :isOld="isOld"
                  :comparisonBlockData="comparisonBlockData"
                  @show-comparison="emitShowComparison"
                  @update-block-info="updateBlockInfo"
                  @update-comparison-block-info="updateComparisonBlockInfo"
                  @close-comparison="closeComparePopup"/>
    <CityInfoBox @clicked="onClickCityToggleBtn"
                 :simInfo="simulationInfo"
                 :cityData="cityInfo"
                 :isOld="isOld"
                 :hasOldSimulation="hasOldSimulation"/>


  </div>
</template>


<script>
import CityInfoBox from '@/components/CityInfoBox.vue';
import BlockInfoBox from "@/components/BlockInfoBox.vue";
import MetroBlocksScene from "@usi-si-teaching/metroblocks";
import "@usi-si-teaching/metroblocks/style";
import { exampleCitySpecs } from "@usi-si-teaching/metroblocks";
import axios from "../axios";
import { Icon } from "@iconify/vue";
import {toast} from "vue3-toastify";
import "vue3-toastify/dist/index.css";

export default {
  name: "renderCity",
  components: {
    MetroBlocksScene,
    CityInfoBox,
    BlockInfoBox,
    Icon,
    exampleCitySpecs
  },
  props: {
    city: Object,
    blocksToCompare: Object,
    simulationInfo: Object,
    isSplitView: Boolean,
    isOld: Boolean
  },
  computed: {
    cityInfo() {
      const { blocks, ...rest } = this.city;
      return rest;
    },
    hasOldSimulation() {
      return this.simulationInfo.oldSimulation != null;
    }
  },
  mounted() {
    this.$store.state.isOldCityInfoBoxOpen = false
    this.$store.state.isCityInfoBoxOpen = false
    this.$store.state.isOldBlockInfoBoxOpen = false
    this.$store.state.isBlockInfoBoxOpen = false
  },
  data() {
    return {
      officeTileSpec: {
        name: 'Office Building Tile',
        type: 'BuildingOffice',
        meshNameToPath: {
          officeBase: 'Office_base.glb',
          officeFloor: 'Office_floor.glb',
          officeRoof: 'Office_roof.glb',
          palm1: 'Palm_tree_01.glb',
          palm2: 'Palm_tree_02.glb',
          tree1: 'Tree_01.glb',
          tree2: 'Tree_02.glb',
          tree3: 'Tree_03.glb',
        },
        randomProps: {
          propsMeshes: ['palm1', 'palm2', 'tree1', 'tree2', 'tree3'],
          maxCount: 5,
        },
        hasBuilding: true,
        building: {
          center: {x: 0, z: 0},
          floors: [
            {
              type: 'Single',
              mesh: 'officeBase',
            },
            {
              type: 'Stacked',
              mesh: 'officeFloor',
              count: 'numberOfFloors',
            },
            {
              type: 'Single',
              mesh: 'officeRoof',
            },
          ],
        },
      },
      residentialTileSpec: {
        name: 'Residential Building Tile',
        type: 'BuildingResidential',
        meshNameToPath: {
          buildingBase: 'Building_base.glb',
          buildingFloor: 'Building_floor.glb',
          buildingRoof: 'Building_roof.glb',
          palm1: 'Palm_tree_01.glb',
          palm2: 'Palm_tree_02.glb',
          tree1: 'Tree_01.glb',
          tree2: 'Tree_02.glb',
          tree3: 'Tree_03.glb',
        },
        randomProps: {
          propsMeshes: ['palm1', 'palm2', 'tree1', 'tree2', 'tree3'],
          maxCount: 5,
        },
        hasBuilding: true,
        building: {
          center: {x: 0, z: 0},
          floors: [
            {
              type: 'Single',
              mesh: 'buildingBase',
            },
            {
              type: 'Stacked',
              mesh: 'buildingFloor',
              count: 'numberOfFloors',
            },
            {
              type: 'Single',
              mesh: 'buildingRoof',
            },
          ],
        },
      },
      parkTileSpec: {
        name: 'Park Tile',
        type: 'Park',
        meshNameToPath: {
          palm1: 'Palm_tree_01.glb',
          palm2: 'Palm_tree_02.glb',
          tree1: 'Tree_01.glb',
          tree2: 'Tree_02.glb',
          tree3: 'Tree_03.glb',
          hotDog: 'Hot_dog_stand.glb',
          picNick: 'Pic-nic_table.glb'
        },
        randomProps: {
          propsMeshes: ['palm1', 'palm2', 'tree1', 'tree2', 'tree3', 'hotDog', 'picNick', 'tree2', 'tree1', 'tree2',
            'tree2', 'tree2', 'tree1', 'tree1', 'tree1', 'tree1', 'tree2', 'tree1', 'tree2', 'tree1', 'palm1', 'palm2', 'palm2', 'palm1'],
          maxCount: 25,
        },
        hasBuilding: false,
      },
      waterTileSpec: {
        name: 'Water Tile',
        type: 'Water',
        meshNameToPath: {},
        randomProps: {
          propsMeshes: [],
          maxCount: 0,
        },
        hasBuilding: false,
      },
      citySpec: {
        id: 'id',
        name: 'name',
        radius: 0,
        tileSpecs: [],
        tileInstanceGroups: [
          {
            tileSpecName: "",
            instances: [], //array of obj position and floor count
          },
          {
            tileSpecName: "",
            instances: [],
          },
          {
            tileSpecName: "",
            instances: [],
          },
          {
            tileSpecName: "",
            instances: [],
          }
        ],
      },
      selectedDataTile: {
        cityId: "",
        position: {"x": "-1", "z": "-1"},
        tileType: ""
      },
      isBlockInfoBoxVisible: false,
      isCityInfoBoxOpen: false,
      blockInfoBoxTransitionStyle: 'right 0.3s ease, transform 0.2s ease-out',
      blockData: {
        position: {"x": "-1", "z": "-1"},
        blockType: "",
        building: {height: 0, people: 0, buildingType: ''},
        buildingType: "",
        rentCost: {value: 0, currency: ''},
        landPrice: {value: 0, currency: ''},
        transportationCost: 0.0,
        constructionCost: 0.0,
        localLimitRentCost: 0.0,
        localLimitTransportationCost: 0.0,
        localLimitConstructionCost: {value: 0, currency: ''}
      },
      isCitySpecPopulated: false,
      mapFetched: true,
      comparisonBlockData: {}
    }
  },
  created() {
    this.populateCitySpecInstances();
  },
  watch: {
    '$route': function (newRoute) {
      this.checkIfPublicRoute();
    },
    isSplitView(newVal) {
      this.isSplitView = newVal;
    }
  },
  methods: {
    handleTileSelection(tileData) {
      this.selectedDataTile = tileData;
      let x = this.selectedDataTile.position.x;
      let z = this.selectedDataTile.position.z;
      this.getBlockInfo(x, z);
      this.blockData.position = this.selectedDataTile.position;
      this.isBlockInfoBoxVisible = true;
    },
    handleTileDeselection() {
      if (this.isOld) {
        this.$store.state.isOldBlockInfoBoxOpen = false
      } else {
        this.$store.state.isBlockInfoBoxOpen = false
      }

      this.isBlockInfoBoxVisible = false;
    },
    onClickCityToggleBtn() {
      this.isCityInfoBoxOpen = !this.isCityInfoBoxOpen;
      if (this.isCityInfoBoxOpen) {
        this.blockInfoBoxTransitionStyle = 'right 0.3s ease-out, transform 0.2s ease-out';
      } else {
        this.blockInfoBoxTransitionStyle = 'right 0.3s ease-out, transform 0.4s ease-in 0.2s';
      }
    },
    async getBlockInfo(x, z) {
      let field = `Coordinates[x=${x}, y=${z}]`;
      this.blockData = {...this.city.blocks[field], coordinates: {x, z}};


      console.log('get block info')
      console.log(this.blockData)


      if (this.blocksToCompare && this.blocksToCompare[field]) {
        this.comparisonBlockData = {...this.blocksToCompare[field], coordinates: {x, z}};
        console.log('old block info')
        console.log(this.comparisonBlockData)

      } else {
        this.comparisonBlockData = null;
      }
    },
    emitShowComparison(currentBlock, comparisonBlock) {
      this.$emit('show-comparison', currentBlock, comparisonBlock);
    },
    updateBlockInfo(newBlockInfo) {
      this.$emit('update-block-info', newBlockInfo);
    },
    updateComparisonBlockInfo(newComparisonBlockInfo) {
      this.$emit('update-comparison-block-info', newComparisonBlockInfo);
    },
    closeComparePopup() {
      this.$emit('close-comparison');
    },
    populateCitySpecInstances() {
      const cityRadius = this.city.radius;
      this.$store.state.isCompareMode = false;
      this.citySpec.radius = cityRadius;
      this.citySpec.tileSpecs = [this.parkTileSpec, this.waterTileSpec, this.residentialTileSpec, this.officeTileSpec];
      this.citySpec.tileInstanceGroups[0].tileSpecName = this.residentialTileSpec.name;
      this.citySpec.tileInstanceGroups[1].tileSpecName = this.officeTileSpec.name;
      this.citySpec.tileInstanceGroups[2].tileSpecName = this.parkTileSpec.name;
      this.citySpec.tileInstanceGroups[3].tileSpecName = this.waterTileSpec.name;
      for (let x = -cityRadius; x <= cityRadius; x++) {
        for (let z = -cityRadius; z <= cityRadius; z++) {
          let field = `Coordinates[x=${x}, y=${z}]`;
          let currentBlockData = this.city.blocks[field];
          switch (currentBlockData.blockType) {
            case "WATERBLOCK":
              this.citySpec.tileInstanceGroups[3].instances.push(
                  {
                    position: { "x": x, "z": z }
                  }
              )
              break;
            case "PARKBLOCK":
              this.citySpec.tileInstanceGroups[2].instances.push(
                  {
                    position: { "x": x, "z": z }
                  }
              )
              break;
            case "RESIDENTIALBLOCK":
              if (currentBlockData.building.buildingType === "HOUSE" || currentBlockData.building.buildingType === "CONDO") {
                this.citySpec.tileInstanceGroups[0].instances.push(
                    {
                      position: { "x": x, "z": z },
                      floorCount: currentBlockData.building.height
                    }
                )
              }
              //if it is SKYSCRAPER:
              else {
                this.citySpec.tileInstanceGroups[1].instances.push(
                    {
                      position: { "x": x, "z": z },
                      floorCount: currentBlockData.building.height
                    }
                )
              }
              break;
          }
        }
      }
      this.isCitySpecPopulated = true;
    },

  }

}

</script>

<style scoped>

.city-simulation {
  height: calc(100vh - 110px);
}

.header button:last-child {
  margin-right: 0;
}
.shifted-down {
  transform: translateY(80px);
}

.loading-screen {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.name-box {
  position: absolute;
  transform: translateX(-50%);
  left: 150px;
  top: 20px;
  width: 150px;
  max-height: 80%;
}
</style>
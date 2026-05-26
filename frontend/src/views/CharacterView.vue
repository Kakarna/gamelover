<script setup lang="ts">
import { ref, computed } from 'vue'
import MainLayout from '@/components/MainLayout.vue'
import { useUserStore } from '@/stores/user'
import {
  User,
  Sword,
  Sparkles,
  Zap,
  Shield,
  TrendingUp,
  X,
  CheckCircle,
  AlertCircle,
  Snowflake,
  Flame,
  Wind,
  Crosshair
} from 'lucide-vue-next'

const userStore = useUserStore()

interface Character {
  id: string
  name: string
  element: string
  weapon: string
  level: number
  breakthrough: number
  skills: {
    basic: number
    skill: number
    ultimate: number
    talent: number
  }
  cost: number
  syncStatus: '已同步' | '未同步'
  rarity: number
  faction?: string
  description?: string
}

const characters = ref<Character[]>([
  {
    id: '1',
    name: '绯雪',
    element: '冷凝',
    weapon: '迅刀',
    level: 80,
    breakthrough: 6,
    skills: { basic: 8, skill: 10, ultimate: 10, talent: 8 },
    cost: 4,
    syncStatus: '已同步',
    rarity: 5,
    faction: '黑海岸',
    description: '黑海岸的年轻指挥官，擅长冷凝元素攻击'
  },
  {
    id: '2',
    name: '达妮娅',
    element: '热熔',
    weapon: '音感仪',
    level: 80,
    breakthrough: 6,
    skills: { basic: 6, skill: 8, ultimate: 10, talent: 6 },
    cost: 4,
    syncStatus: '已同步',
    rarity: 5,
    faction: '星门',
    description: '星门天才科学家，以热熔射线瓦解敌人'
  },
  {
    id: '3',
    name: '折枝',
    element: '冷凝',
    weapon: '迅刀',
    level: 70,
    breakthrough: 5,
    skills: { basic: 6, skill: 6, ultimate: 8, talent: 5 },
    cost: 3,
    syncStatus: '已同步',
    rarity: 4,
    faction: '民间组织',
    description: '民间组织成员，冷凝系迅刀专家'
  },
  {
    id: '4',
    name: '冽',
    element: '冷凝',
    weapon: '臂铠',
    level: 60,
    breakthrough: 4,
    skills: { basic: 4, skill: 5, ultimate: 6, talent: 4 },
    cost: 2,
    syncStatus: '未同步',
    rarity: 4,
    faction: '降魔小队',
    description: '降魔小队成员，近战格斗专家'
  }
])

const selectedCharacter = ref<Character | null>(null)
const showModal = ref(false)

const getElementInfo = (element: string) => {
  const info: Record<string, { color: string; bg: string; icon: any }> = {
    '冷凝': { color: '#38bdf8', bg: 'rgba(56, 189, 248, 0.15)', icon: Snowflake },
    '热熔': { color: '#f97316', bg: 'rgba(249, 115, 22, 0.15)', icon: Flame },
    '风': { color: '#22d3ee', bg: 'rgba(34, 211, 238, 0.15)', icon: Wind },
    '雷': { color: '#a855f7', bg: 'rgba(168, 85, 247, 0.15)', icon: Zap },
    '物理': { color: '#ef4444', bg: 'rgba(239, 68, 68, 0.15)', icon: Crosshair }
  }
  return info[element] || info['物理']
}

const getWeaponInfo = (weapon: string) => {
  const info: Record<string, { color: string; bg: string }> = {
    '迅刀': { color: '#6366f1', bg: 'rgba(99, 102, 241, 0.15)' },
    '音感仪': { color: '#ec4899', bg: 'rgba(236, 72, 153, 0.15)' },
    '臂铠': { color: '#f59e0b', bg: 'rgba(245, 158, 11, 0.15)' },
    '火铳': { color: '#ef4444', bg: 'rgba(239, 68, 68, 0.15)' },
    '长柄': { color: '#10b981', bg: 'rgba(16, 185, 129, 0.15)' }
  }
  return info[weapon] || info['迅刀']
}

const showCharacterDetail = (character: Character) => {
  selectedCharacter.value = character
  showModal.value = true
}

const closeModal = () => {
  showModal.value = false
  selectedCharacter.value = null
}

const getOverallPower = computed(() => {
  if (!selectedCharacter.value) return 0
  const c = selectedCharacter.value
  return Math.round((c.skills.basic + c.skills.skill + c.skills.ultimate + c.skills.talent) * 10 + c.level * 2)
})

const getSkillTotal = computed(() => {
  if (!selectedCharacter.value) return 0
  const s = selectedCharacter.value.skills
  return s.basic + s.skill + s.ultimate + s.talent
})

const getMaxSkillLevel = computed(() => {
  if (!selectedCharacter.value) return 0
  return Math.max(
    selectedCharacter.value.skills.basic,
    selectedCharacter.value.skills.skill,
    selectedCharacter.value.skills.ultimate,
    selectedCharacter.value.skills.talent
  )
})
</script>

<template>
  <MainLayout>
    <div class="character-view">
      <div class="page-header">
        <div class="page-title-section">
          <div class="title-icon">
            <Sword :size="32" />
          </div>
          <div class="title-content">
            <h1 class="page-title">
              <span class="gradient-text-mystic">角色练度</span>
            </h1>
            <div class="title-divider"></div>
            <p class="page-subtitle">查看你的角色培养情况，了解实力提升空间</p>
          </div>
        </div>
      </div>

      <n-grid :cols="4" :x-gap="20" :y-gap="20" class="characters-grid">
        <n-gi v-for="char in characters" :key="char.id">
          <n-card
            class="character-card tarot-card"
            @click="showCharacterDetail(char)"
          >
            <div class="card-glow" :style="{ background: getElementInfo(char.element).bg }"></div>

            <div class="character-header">
              <div
                class="character-avatar"
                :style="{
                  background: `linear-gradient(135deg, ${getElementInfo(char.element).bg}, rgba(26, 26, 62, 0.8))`,
                  borderColor: getElementInfo(char.element).color
                }"
              >
                <User :size="32" :style="{ color: getElementInfo(char.element).color }" />
                <div class="rarity-stars">
                  <span v-for="i in char.rarity" :key="i" class="star">★</span>
                </div>
              </div>

              <div class="character-info">
                <div class="name-row">
                  <h3 class="character-name">{{ char.name }}</h3>
                </div>
                <div class="tags-row">
                  <span
                    class="element-tag"
                    :style="{
                      background: getElementInfo(char.element).bg,
                      color: getElementInfo(char.element).color,
                      borderColor: getElementInfo(char.element).color + '40'
                    }"
                  >
                    <component :is="getElementInfo(char.element).icon" :size="12" />
                    {{ char.element }}
                  </span>
                  <span
                    class="weapon-tag"
                    :style="{
                      background: getWeaponInfo(char.weapon).bg,
                      color: getWeaponInfo(char.weapon).color,
                      borderColor: getWeaponInfo(char.weapon).color + '40'
                    }"
                  >
                    <Sword :size="12" />
                    {{ char.weapon }}
                  </span>
                </div>
              </div>
            </div>

            <div class="sync-status" :class="char.syncStatus === '已同步' ? 'synced' : 'not-synced'">
              <CheckCircle v-if="char.syncStatus === '已同步'" :size="14" />
              <AlertCircle v-else :size="14" />
              {{ char.syncStatus }}
            </div>

            <div class="level-section">
              <div class="level-info">
                <span class="level-label">等级</span>
                <span class="level-value">Lv.{{ char.level }}</span>
              </div>
              <div class="breakthrough-info">
                <span class="breakthrough-label">突破</span>
                <div class="breakthrough-dots">
                  <span
                    v-for="i in 6"
                    :key="i"
                    :class="['dot', { active: i <= char.breakthrough }]"
                  ></span>
                </div>
              </div>
            </div>

            <div class="skills-preview">
              <div class="skill-item">
                <span class="skill-name">普攻</span>
                <div class="skill-bar-bg">
                  <div
                    class="skill-bar-fill"
                    :style="{
                      width: (char.skills.basic / 10) * 100 + '%',
                      background: `linear-gradient(90deg, ${getElementInfo(char.element).color}, var(--persona-gold))`
                    }"
                  ></div>
                </div>
                <span class="skill-num">{{ char.skills.basic }}</span>
              </div>
              <div class="skill-item">
                <span class="skill-name">技能</span>
                <div class="skill-bar-bg">
                  <div
                    class="skill-bar-fill"
                    :style="{
                      width: (char.skills.skill / 10) * 100 + '%',
                      background: `linear-gradient(90deg, ${getElementInfo(char.element).color}, var(--persona-gold))`
                    }"
                  ></div>
                </div>
                <span class="skill-num">{{ char.skills.skill }}</span>
              </div>
              <div class="skill-item">
                <span class="skill-name">共鸣</span>
                <div class="skill-bar-bg">
                  <div
                    class="skill-bar-fill"
                    :style="{
                      width: (char.skills.ultimate / 10) * 100 + '%',
                      background: `linear-gradient(90deg, ${getElementInfo(char.element).color}, var(--persona-gold))`
                    }"
                  ></div>
                </div>
                <span class="skill-num">{{ char.skills.ultimate }}</span>
              </div>
              <div class="skill-item">
                <span class="skill-name">天赋</span>
                <div class="skill-bar-bg">
                  <div
                    class="skill-bar-fill"
                    :style="{
                      width: (char.skills.talent / 10) * 100 + '%',
                      background: `linear-gradient(90deg, ${getElementInfo(char.element).color}, var(--persona-gold))`
                    }"
                  ></div>
                </div>
                <span class="skill-num">{{ char.skills.talent }}</span>
              </div>
            </div>
          </n-card>
        </n-gi>
      </n-grid>

      <n-modal
        v-model:show="showModal"
        preset="card"
        class="character-modal"
        :style="{ maxWidth: '580px' }"
        :mask-closable="true"
        :bordered="false"
      >
        <template #header>
          <div v-if="selectedCharacter" class="modal-header-custom">
            <div class="modal-title-icon">
              <User :size="20" />
            </div>
            <div class="modal-title-info">
              <span class="modal-title">{{ selectedCharacter.name }}</span>
              <span class="modal-subtitle">{{ selectedCharacter.faction }}</span>
            </div>
          </div>
        </template>

        <div v-if="selectedCharacter" class="modal-content">
          <div class="modal-hero">
            <div
              class="hero-avatar"
              :style="{
                background: `linear-gradient(135deg, ${getElementInfo(selectedCharacter.element).bg}, rgba(26, 26, 62, 0.9))`,
                borderColor: getElementInfo(selectedCharacter.element).color,
                boxShadow: `0 0 30px ${getElementInfo(selectedCharacter.element).color}40`
              }"
            >
              <User :size="56" :style="{ color: getElementInfo(selectedCharacter.element).color }" />
            </div>

            <div class="hero-info">
              <div class="hero-tags">
                <span
                  class="element-badge"
                  :style="{
                    background: getElementInfo(selectedCharacter.element).bg,
                    color: getElementInfo(selectedCharacter.element).color,
                    borderColor: getElementInfo(selectedCharacter.element).color + '40'
                  }"
                >
                  <component :is="getElementInfo(selectedCharacter.element).icon" :size="14" />
                  {{ selectedCharacter.element }}
                </span>
                <span
                  class="weapon-badge"
                  :style="{
                    background: getWeaponInfo(selectedCharacter.weapon).bg,
                    color: getWeaponInfo(selectedCharacter.weapon).color,
                    borderColor: getWeaponInfo(selectedCharacter.weapon).color + '40'
                  }"
                >
                  <Sword :size="14" />
                  {{ selectedCharacter.weapon }}
                </span>
              </div>
              <p class="hero-description">{{ selectedCharacter.description }}</p>
            </div>
          </div>

          <div class="power-section">
            <div class="power-card glow-gold">
              <div class="power-icon">
                <Zap :size="24" />
              </div>
              <div class="power-info">
                <span class="power-label">综合战力</span>
                <span class="power-value">{{ getOverallPower }}</span>
              </div>
            </div>
            <div class="power-card">
              <div class="power-icon">
                <TrendingUp :size="24" />
              </div>
              <div class="power-info">
                <span class="power-label">技能总等级</span>
                <span class="power-value">{{ getSkillTotal }}</span>
              </div>
            </div>
            <div class="power-card">
              <div class="power-icon">
                <Shield :size="24" />
              </div>
              <div class="power-info">
                <span class="power-label">最高技能</span>
                <span class="power-value">{{ getMaxSkillLevel }}</span>
              </div>
            </div>
          </div>

          <div class="divider-mystic"></div>

          <div class="detail-section">
            <h4 class="section-title">
              <Sparkles :size="18" />
              培养详情
            </h4>

            <div class="detail-grid">
              <div class="detail-item">
                <span class="detail-label">等级</span>
                <span class="detail-value">Lv.{{ selectedCharacter.level }}</span>
              </div>
              <div class="detail-item">
                <span class="detail-label">突破</span>
                <span class="detail-value">{{ selectedCharacter.breakthrough }}/6</span>
              </div>
              <div class="detail-item">
                <span class="detail-label">声骸</span>
                <span class="detail-value">COST {{ selectedCharacter.cost }}</span>
              </div>
              <div class="detail-item">
                <span class="detail-label">稀有度</span>
                <span class="detail-value stars">
                  <span v-for="i in selectedCharacter.rarity" :key="i" class="star">★</span>
                </span>
              </div>
            </div>
          </div>

          <div class="skills-section">
            <h4 class="section-title">
              <Sword :size="18" />
              技能等级
            </h4>

            <div class="skills-grid">
              <div class="skill-detail-card">
                <span class="skill-detail-name">普攻</span>
                <div class="skill-detail-bar">
                  <div
                    class="skill-detail-fill"
                    :style="{
                      width: (selectedCharacter.skills.basic / 10) * 100 + '%',
                      background: `linear-gradient(90deg, ${getElementInfo(selectedCharacter.element).color}, var(--persona-gold))`
                    }"
                  ></div>
                </div>
                <span class="skill-detail-level">{{ selectedCharacter.skills.basic }}/10</span>
              </div>

              <div class="skill-detail-card">
                <span class="skill-detail-name">技能</span>
                <div class="skill-detail-bar">
                  <div
                    class="skill-detail-fill"
                    :style="{
                      width: (selectedCharacter.skills.skill / 10) * 100 + '%',
                      background: `linear-gradient(90deg, ${getElementInfo(selectedCharacter.element).color}, var(--persona-gold))`
                    }"
                  ></div>
                </div>
                <span class="skill-detail-level">{{ selectedCharacter.skills.skill }}/10</span>
              </div>

              <div class="skill-detail-card">
                <span class="skill-detail-name">共鸣</span>
                <div class="skill-detail-bar">
                  <div
                    class="skill-detail-fill"
                    :style="{
                      width: (selectedCharacter.skills.ultimate / 10) * 100 + '%',
                      background: `linear-gradient(90deg, ${getElementInfo(selectedCharacter.element).color}, var(--persona-gold))`
                    }"
                  ></div>
                </div>
                <span class="skill-detail-level">{{ selectedCharacter.skills.ultimate }}/10</span>
              </div>

              <div class="skill-detail-card">
                <span class="skill-detail-name">天赋</span>
                <div class="skill-detail-bar">
                  <div
                    class="skill-detail-fill"
                    :style="{
                      width: (selectedCharacter.skills.talent / 10) * 100 + '%',
                      background: `linear-gradient(90deg, ${getElementInfo(selectedCharacter.element).color}, var(--persona-gold))`
                    }"
                  ></div>
                </div>
                <span class="skill-detail-level">{{ selectedCharacter.skills.talent }}/10</span>
              </div>
            </div>
          </div>
        </div>

        <template #footer>
          <div class="modal-footer">
            <n-button class="btn-secondary" @click="closeModal">
              <X :size="16" />
              关闭
            </n-button>
          </div>
        </template>
      </n-modal>
    </div>
  </MainLayout>
</template>

<style scoped>
.character-view {
  padding: 24px;
}

.page-header {
  margin-bottom: 32px;
}

.page-title-section {
  display: flex;
  align-items: flex-start;
  gap: 20px;
}

.title-icon {
  width: 64px;
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, rgba(212, 168, 75, 0.2), rgba(74, 63, 107, 0.2));
  border: 1px solid rgba(212, 168, 75, 0.3);
  border-radius: 12px;
  color: var(--persona-gold);
}

.title-content {
  padding-top: 4px;
}

.page-title {
  font-size: 28px;
  font-weight: 700;
  margin: 0 0 8px 0;
  line-height: 1.2;
}

.title-divider {
  width: 60px;
  height: 2px;
  background: linear-gradient(90deg, var(--persona-gold), transparent);
  margin-bottom: 8px;
}

.page-subtitle {
  color: var(--persona-silver);
  font-size: 14px;
  margin: 0;
}

.characters-grid {
  margin-bottom: 24px;
}

.character-card {
  position: relative;
  overflow: hidden;
  cursor: pointer;
}

.card-glow {
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  opacity: 0.4;
  filter: blur(60px);
  pointer-events: none;
  transition: opacity 0.3s ease;
}

.character-card:hover .card-glow {
  opacity: 0.6;
}

.character-header {
  display: flex;
  gap: 16px;
  margin-bottom: 16px;
  position: relative;
  z-index: 1;
}

.character-avatar {
  position: relative;
  width: 72px;
  height: 72px;
  border-radius: 12px;
  border: 2px solid;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  transition: all 0.3s ease;
}

.character-card:hover .character-avatar {
  transform: scale(1.05);
}

.rarity-stars {
  position: absolute;
  bottom: -8px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 1px;
}

.star {
  font-size: 10px;
  color: var(--persona-gold);
  text-shadow: 0 0 4px rgba(212, 168, 75, 0.5);
}

.character-info {
  flex: 1;
  min-width: 0;
}

.name-row {
  margin-bottom: 8px;
}

.character-name {
  font-size: 20px;
  font-weight: 700;
  color: var(--color-text);
  margin: 0;
  letter-spacing: 1px;
}

.tags-row {
  display: flex;
  gap: 8px;
}

.element-tag,
.weapon-tag {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 4px 10px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
  border: 1px solid;
}

.sync-status {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 4px 12px;
  border-radius: 4px;
  font-size: 11px;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 1px;
  margin-bottom: 16px;
}

.sync-status.synced {
  background: rgba(16, 185, 129, 0.15);
  color: #10B981;
  border: 1px solid rgba(16, 185, 129, 0.3);
}

.sync-status.not-synced {
  background: rgba(249, 115, 22, 0.15);
  color: #F97316;
  border: 1px solid rgba(249, 115, 22, 0.3);
}

.level-section {
  display: flex;
  justify-content: space-between;
  padding: 16px;
  background: rgba(255, 255, 255, 0.03);
  border-radius: 8px;
  margin-bottom: 16px;
  border: 1px solid rgba(212, 168, 75, 0.1);
}

.level-info,
.breakthrough-info {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.level-label,
.breakthrough-label {
  font-size: 11px;
  color: var(--persona-silver);
  text-transform: uppercase;
  letter-spacing: 1px;
}

.level-value {
  font-size: 18px;
  font-weight: 700;
  color: var(--persona-gold);
}

.breakthrough-dots {
  display: flex;
  gap: 6px;
}

.dot {
  width: 10px;
  height: 10px;
  border-radius: 2px;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(212, 168, 75, 0.2);
  transition: all 0.3s ease;
}

.dot.active {
  background: linear-gradient(135deg, var(--persona-gold), var(--persona-gold-light));
  border-color: var(--persona-gold);
  box-shadow: 0 0 8px rgba(212, 168, 75, 0.5);
}

.skills-preview {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.skill-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.skill-name {
  width: 36px;
  font-size: 11px;
  color: var(--persona-silver);
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.skill-bar-bg {
  flex: 1;
  height: 4px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 2px;
  overflow: hidden;
}

.skill-bar-fill {
  height: 100%;
  border-radius: 2px;
  transition: width 0.5s cubic-bezier(0.23, 1, 0.32, 1);
}

.skill-num {
  width: 24px;
  font-size: 12px;
  font-weight: 600;
  color: var(--persona-gold);
  text-align: right;
}

.modal-header-custom {
  display: flex;
  align-items: center;
  gap: 12px;
}

.modal-title-icon {
  color: var(--persona-gold);
}

.modal-title-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.modal-title {
  font-size: 20px;
  font-weight: 700;
  color: var(--persona-gold-light);
  letter-spacing: 1px;
}

.modal-subtitle {
  font-size: 12px;
  color: var(--persona-silver);
}

.modal-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.modal-hero {
  display: flex;
  gap: 24px;
  align-items: flex-start;
}

.hero-avatar {
  width: 100px;
  height: 100px;
  border-radius: 12px;
  border: 2px solid;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  transition: all 0.4s ease;
}

.hero-info {
  flex: 1;
}

.hero-tags {
  display: flex;
  gap: 10px;
  margin-bottom: 12px;
}

.element-badge,
.weapon-badge {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 6px 14px;
  border-radius: 4px;
  font-size: 13px;
  font-weight: 600;
  border: 1px solid;
  letter-spacing: 0.5px;
}

.hero-description {
  color: var(--persona-silver);
  font-size: 14px;
  line-height: 1.6;
  margin: 0;
}

.power-section {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}

.power-card {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background: rgba(26, 26, 62, 0.6);
  border: 1px solid rgba(212, 168, 75, 0.15);
  border-radius: 8px;
  transition: all 0.3s ease;
}

.power-card:hover {
  border-color: rgba(212, 168, 75, 0.3);
  background: rgba(36, 36, 72, 0.6);
}

.power-icon {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(212, 168, 75, 0.1);
  border-radius: 8px;
  color: var(--persona-gold);
}

.power-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.power-label {
  font-size: 11px;
  color: var(--persona-silver);
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.power-value {
  font-size: 22px;
  font-weight: 700;
  color: var(--persona-gold-light);
}

.detail-section,
.skills-section {
  margin-top: 8px;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  font-weight: 600;
  color: var(--persona-gold);
  margin: 0 0 16px 0;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.detail-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

.detail-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background: rgba(26, 26, 62, 0.4);
  border: 1px solid rgba(212, 168, 75, 0.1);
  border-radius: 6px;
}

.detail-label {
  font-size: 12px;
  color: var(--persona-silver);
}

.detail-value {
  font-size: 14px;
  font-weight: 600;
  color: var(--persona-gold-light);
}

.detail-value.stars {
  display: flex;
  gap: 2px;
}

.skills-grid {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.skill-detail-card {
  display: flex;
  align-items: center;
  gap: 12px;
}

.skill-detail-name {
  width: 48px;
  font-size: 12px;
  color: var(--persona-silver);
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.skill-detail-bar {
  flex: 1;
  height: 8px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 4px;
  overflow: hidden;
}

.skill-detail-fill {
  height: 100%;
  border-radius: 4px;
  transition: width 0.6s cubic-bezier(0.23, 1, 0.32, 1);
}

.skill-detail-level {
  width: 48px;
  font-size: 13px;
  font-weight: 600;
  color: var(--persona-gold);
  text-align: right;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
}

.btn-secondary {
  background: rgba(26, 26, 62, 0.8) !important;
  color: var(--persona-silver) !important;
  border: 1px solid rgba(212, 168, 75, 0.2) !important;
  font-weight: 600;
  letter-spacing: 1px;
  text-transform: uppercase;
  font-size: 13px;
}

.btn-secondary:hover {
  border-color: var(--persona-gold) !important;
  color: var(--persona-gold-light) !important;
}

:deep(.character-modal) {
  --n-border: 1px solid rgba(212, 168, 75, 0.2) !important;
  --n-header-border: none !important;
  --n-color: rgba(26, 26, 62, 0.98) !important;
  --n-title-text-color: var(--persona-gold) !important;
  --n-action-border-radius: 4px !important;
}
</style>

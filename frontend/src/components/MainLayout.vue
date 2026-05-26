<script setup lang="ts">
import { ref, watch, computed, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import {
  Home,
  Bell,
  Gift,
  User,
  Settings,
  Gamepad2,
  Sword,
  BarChart3,
  ChevronLeft,
  ChevronRight,
  Sparkles,
  Layers,
  Zap,
  Moon,
  Sun,
  Star
} from 'lucide-vue-next'

const collapsed = ref(false)
const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const currentMoonPhase = ref(0)
const showParticles = ref(true)
const showSakura = ref(true)
const stars = ref<Array<{ id: number; x: number; y: number; size: number; delay: number }>>([])
const sakuraPetals = ref<Array<{ id: number; x: number; size: number; delay: number; duration: number; rotation: number }>>([])
let moonPhaseInterval: number | null = null

const menuItems = [
  { path: '/', label: '首页', icon: Home, tarot: '愚者' },
  { path: '/announcement', label: '公告', icon: Bell, tarot: '命运之轮' },
  { path: '/gacha', label: '抽卡记录', icon: Gift, tarot: '星星' },
  { path: '/character', label: '角色练度', icon: Sword, tarot: '力量' },
  { path: '/statistics', label: '数据统计', icon: BarChart3, tarot: '正义' },
  { path: '/settings', label: '设置', icon: Settings, tarot: '魔术师' },
]

const activeMenu = ref(route.path)

const gameOptions = [
  { label: '🌊 鸣潮', value: 'WUWA' },
  { label: '🚂 崩铁', value: 'HSR' },
  { label: '✨ 原神', value: 'Genshin' },
]

const selectedGame = ref(userStore.selectedGame)

const moonPhases = [
  { name: '新月', visible: 0, glow: 0.3 },
  { name: '蛾眉月', visible: 0.25, glow: 0.4 },
  { name: '上弦月', visible: 0.5, glow: 0.6 },
  { name: '盈凸月', visible: 0.75, glow: 0.7 },
  { name: '满月', visible: 1, glow: 1 },
  { name: '亏凸月', visible: 0.75, glow: 0.7 },
  { name: '下弦月', visible: 0.5, glow: 0.6 },
  { name: '残月', visible: 0.25, glow: 0.4 },
]

const currentMoonPhaseData = computed(() => moonPhases[currentMoonPhase.value])

watch(() => route.path, (newPath) => {
  activeMenu.value = newPath
})

const handleMenuClick = (path: string) => {
  activeMenu.value = path
  router.push(path)
}

const handleGameChange = (value: string) => {
  userStore.setSelectedGame(value)
}

const toggleSidebar = () => {
  collapsed.value = !collapsed.value
}

const sidebarWidth = computed(() => collapsed.value ? '80px' : '260px')

const moonPosition = computed(() => {
  switch (userStore.currentTheme) {
    case 'day': return { top: '10%', right: '15%' }
    case 'sunset': return { top: '20%', right: '25%' }
    default: return { top: '15%', right: '10%' }
  }
})

onMounted(() => {
  document.documentElement.setAttribute('data-theme',
    userStore.currentTheme === 'day' ? 'day' : userStore.currentTheme === 'sunset' ? 'sunset' : ''
  )

  const generatedStars = []
  for (let i = 0; i < 80; i++) {
    generatedStars.push({
      id: i,
      x: Math.random() * 100,
      y: Math.random() * 100,
      size: Math.random() * 2.5 + 0.5,
      delay: Math.random() * 4
    })
  }
  stars.value = generatedStars

  const generatedPetals = []
  for (let i = 0; i < 30; i++) {
    generatedPetals.push({
      id: i,
      x: Math.random() * 100,
      size: Math.random() * 8 + 6,
      delay: Math.random() * 15,
      duration: Math.random() * 10 + 15,
      rotation: Math.random() * 360
    })
  }
  sakuraPetals.value = generatedPetals

  moonPhaseInterval = window.setInterval(() => {
    currentMoonPhase.value = (currentMoonPhase.value + 1) % moonPhases.length
  }, 10000)
})

onUnmounted(() => {
  if (moonPhaseInterval !== null) {
    clearInterval(moonPhaseInterval)
  }
})
</script>

<template>
  <div class="main-layout">
    <div class="animated-background">
      <div class="gradient-circle mystic-1"></div>
      <div class="gradient-circle mystic-2"></div>
      <div class="gradient-circle mystic-3"></div>

      <div class="blue-overlay"></div>

      <div class="star-field">
        <div
          v-for="star in stars"
          :key="star.id"
          class="star"
          :style="{
            left: star.x + '%',
            top: star.y + '%',
            width: star.size + 'px',
            height: star.size + 'px',
            animationDelay: star.delay + 's'
          }"
        ></div>
      </div>

      <transition name="moon-fade" mode="out-in">
        <div
          v-if="userStore.currentTheme !== 'day'"
          class="moon-container"
          :style="{
            top: moonPosition.top,
            right: moonPosition.right,
            '--moon-visible': currentMoonPhaseData.visible,
            '--moon-glow': currentMoonPhaseData.glow
          }"
        >
          <div class="moon-glow"></div>
          <div class="moon-body">
            <div class="moon-illumination"></div>
          </div>
          <div class="moon-phase-label">{{ currentMoonPhaseData.name }}</div>
        </div>
      </transition>

      <div v-if="showSakura" class="sakura-field">
        <div
          v-for="petal in sakuraPetals"
          :key="petal.id"
          class="sakura-petal"
          :style="{
            left: petal.x + '%',
            width: petal.size + 'px',
            height: petal.size * 1.5 + 'px',
            animationDelay: petal.delay + 's',
            animationDuration: petal.duration + 's',
            transform: `rotate(${petal.rotation}deg)`
          }"
        ></div>
      </div>

      <div v-if="showParticles" class="particles">
        <div
          v-for="i in 25"
          :key="'particle-' + i"
          class="particle"
          :style="{
            left: Math.random() * 100 + '%',
            animationDuration: (Math.random() * 10 + 10) + 's',
            animationDelay: Math.random() * 10 + 's',
            opacity: Math.random() * 0.5 + 0.2
          }"
        ></div>
      </div>

      <div class="mesh-gradient"></div>
    </div>

    <n-layout has-sider position="absolute" class="layout-container">
      <n-layout-sider
        bordered
        collapse-mode="width"
        :collapsed-width="80"
        :width="260"
        :collapsed="collapsed"
        show-trigger="bar"
        @collapse="collapsed = true"
        @expand="collapsed = false"
        class="sidebar sidebar-glass"
        :style="{ width: sidebarWidth }"
      >
        <div class="sidebar-content">
          <div class="logo-area">
            <div class="logo-icon-wrapper">
              <Gamepad2 :size="collapsed ? 28 : 32" class="logo-icon" />
              <div class="logo-glow"></div>
            </div>
            <transition name="fade">
              <div v-if="!collapsed" class="logo-text-wrapper">
                <h1 class="logo-title">GameLover</h1>
                <div class="logo-divider"></div>
                <span class="logo-subtitle">游戏数据平台</span>
              </div>
            </transition>
          </div>

          <div class="menu-section">
            <div v-if="!collapsed" class="menu-title">
              <Layers :size="14" />
              <span>阿尔卡纳</span>
            </div>
            <nav class="nav-menu">
              <div
                v-for="item in menuItems"
                :key="item.path"
                :class="['menu-item tarot-menu-item', { active: activeMenu === item.path, collapsed: collapsed }]"
                @click="handleMenuClick(item.path)"
              >
                <div class="menu-item-icon">
                  <component :is="item.icon" :size="20" />
                </div>
                <transition name="fade">
                  <div v-if="!collapsed" class="menu-item-text">
                    <span class="menu-item-label">{{ item.label }}</span>
                    <span class="menu-item-tarot">{{ item.tarot }}</span>
                  </div>
                </transition>
                <div v-if="activeMenu === item.path" class="active-indicator"></div>
              </div>
            </nav>
          </div>

          <div class="sidebar-footer">
            <div class="theme-toggle" @click="userStore.toggleTheme">
              <Moon v-if="userStore.currentTheme === 'night'" :size="16" class="theme-icon" />
              <Sun v-else-if="userStore.currentTheme === 'sunset'" :size="16" class="theme-icon sunset" />
              <Star v-else :size="16" class="theme-icon day" />
            </div>
            <button class="collapse-btn" @click="toggleSidebar">
              <ChevronLeft v-if="!collapsed" :size="18" />
              <ChevronRight v-else :size="18" />
            </button>
          </div>
        </div>
      </n-layout-sider>

      <n-layout class="main-content">
        <n-layout-header class="header header-glass">
          <div class="header-left">
            <div class="breadcrumb">
              <Sparkles :size="18" class="breadcrumb-icon" />
              <span class="breadcrumb-text">当前游戏</span>
            </div>
            <n-select
              v-model:value="selectedGame"
              :options="gameOptions"
              size="large"
              class="game-selector"
              :style="{ width: '180px' }"
              @update:value="handleGameChange"
            />
          </div>

          <div class="header-right">
            <n-badge :value="3" :max="99" type="error">
              <n-button quaternary circle class="header-btn">
                <template #icon>
                  <Bell :size="20" />
                </template>
              </n-button>
            </n-badge>

            <n-dropdown trigger="click" :options="[]">
              <div class="user-profile">
                <n-avatar round size="large" class="avatar">
                  <User :size="20" />
                </n-avatar>
                <div class="user-info">
                  <span class="username">特别课外活动部</span>
                  <span class="user-level">
                    <Zap :size="12" />
                    SEES
                  </span>
                </div>
              </div>
            </n-dropdown>
          </div>
        </n-layout-header>

        <n-layout-content class="content">
          <div class="content-wrapper">
            <slot></slot>
          </div>
        </n-layout-content>
      </n-layout>
    </n-layout>
  </div>
</template>

<style scoped>
.main-layout {
  position: relative;
  width: 100vw;
  height: 100vh;
  overflow: hidden;
}

.animated-background {
  position: absolute;
  inset: 0;
  overflow: hidden;
  z-index: 0;
  background: linear-gradient(180deg,
    #0a0a1f 0%,
    #0d1033 30%,
    #1a1a3e 60%,
    #0a0a1f 100%
  );
}

.blue-overlay {
  position: absolute;
  inset: 0;
  background:
    radial-gradient(ellipse at 20% 30%, rgba(65, 105, 225, 0.15) 0%, transparent 50%),
    radial-gradient(ellipse at 80% 70%, rgba(147, 51, 234, 0.1) 0%, transparent 50%),
    linear-gradient(180deg, rgba(20, 20, 60, 0.4) 0%, transparent 50%);
  pointer-events: none;
  animation: blue-pulse 8s ease-in-out infinite;
}

@keyframes blue-pulse {
  0%, 100% { opacity: 0.8; }
  50% { opacity: 1; }
}

.mesh-gradient {
  position: absolute;
  inset: 0;
  background:
    radial-gradient(at 40% 20%, rgba(74, 63, 107, 0.25) 0px, transparent 50%),
    radial-gradient(at 80% 0%, rgba(139, 21, 56, 0.2) 0px, transparent 50%),
    radial-gradient(at 0% 50%, rgba(212, 168, 75, 0.12) 0px, transparent 50%);
  pointer-events: none;
}

.moon-container {
  position: absolute;
  z-index: 3;
  transition: all 1s cubic-bezier(0.4, 0, 0.2, 1);
}

.moon-glow {
  position: absolute;
  width: 350px;
  height: 350px;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  border-radius: 50%;
  background: radial-gradient(circle, rgba(212, 168, 75, calc(var(--moon-glow, 1) * 0.3)) 0%, transparent 70%);
  filter: blur(50px);
  animation: breathe 4s ease-in-out infinite;
}

.moon-body {
  position: relative;
  width: 140px;
  height: 140px;
  border-radius: 50%;
  background: radial-gradient(circle at 30% 30%,
    #fffde7 0%,
    #ffd54f 25%,
    var(--persona-gold) 60%,
    #8b6914 100%
  );
  animation: moon-pulse 4s ease-in-out infinite;
  filter: blur(0.5px);
  box-shadow:
    0 0 60px rgba(212, 168, 75, calc(var(--moon-glow, 1) * 0.4)),
    0 0 120px rgba(212, 168, 75, calc(var(--moon-glow, 1) * 0.2)),
    inset -15px -15px 50px rgba(0, 0, 0, 0.2);
  overflow: hidden;
}

.moon-illumination {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  background: linear-gradient(90deg, transparent calc((1 - var(--moon-visible, 1)) * 100%), rgba(0, 0, 0, 0.9) calc((1 - var(--moon-visible, 1)) * 100%));
  border-radius: 50%;
  transition: all 1s ease-in-out;
}

.moon-body::before {
  content: "";
  position: absolute;
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background: radial-gradient(circle at 65% 45%, transparent 35%, rgba(0, 0, 0, 0.08) 50%);
}

.moon-body::after {
  content: "";
  position: absolute;
  width: 130%;
  height: 130%;
  top: -15%;
  left: -15%;
  border-radius: 50%;
  border: 1px solid rgba(212, 168, 75, 0.3);
  animation: spin 60s linear infinite;
}

.moon-phase-label {
  position: absolute;
  bottom: -35px;
  left: 50%;
  transform: translateX(-50%);
  color: var(--persona-gold-light);
  font-size: 12px;
  letter-spacing: 3px;
  text-transform: uppercase;
  text-shadow: 0 0 10px rgba(212, 168, 75, 0.5);
  font-weight: 600;
}

.particles {
  position: absolute;
  inset: 0;
  overflow: hidden;
  pointer-events: none;
}

.particle {
  position: absolute;
  bottom: -10px;
  width: 3px;
  height: 3px;
  background: var(--persona-gold);
  border-radius: 50%;
  opacity: 0;
  animation: particle-float linear infinite;
}

@keyframes particle-float {
  0% {
    transform: translateY(0) rotate(0deg);
    opacity: 0;
  }
  10% { opacity: 0.6; }
  90% { opacity: 0.6; }
  100% {
    transform: translateY(-100vh) rotate(360deg);
    opacity: 0;
  }
}

.sakura-field {
  position: absolute;
  inset: 0;
  overflow: hidden;
  pointer-events: none;
  z-index: 5;
}

.sakura-petal {
  position: absolute;
  top: -20px;
  background: linear-gradient(135deg, #ffb7c5 0%, #ff69b4 50%, #ff1493 100%);
  border-radius: 0 100% 0 100%;
  opacity: 0.85;
  animation: sakura-fall linear infinite;
  box-shadow: 0 0 10px rgba(255, 183, 197, 0.5);
}

@keyframes sakura-fall {
  0% {
    transform: translateY(0) rotate(0deg) translateX(0);
    opacity: 0;
  }
  10% {
    opacity: 0.9;
  }
  50% {
    transform: translateY(50vh) rotate(180deg) translateX(30px);
  }
  100% {
    transform: translateY(100vh) rotate(360deg) translateX(-20px);
    opacity: 0;
  }
}

@keyframes breathe {
  0%, 100% { opacity: 0.6; transform: translate(-50%, -50%) scale(1); }
  50% { opacity: 1; transform: translate(-50%, -50%) scale(1.1); }
}

@keyframes moon-pulse {
  0%, 100% {
    box-shadow:
      0 0 60px rgba(212, 168, 75, 0.4),
      0 0 120px rgba(212, 168, 75, 0.2),
      inset -15px -15px 50px rgba(0, 0, 0, 0.2);
  }
  50% {
    box-shadow:
      0 0 80px rgba(212, 168, 75, 0.5),
      0 0 150px rgba(212, 168, 75, 0.3),
      inset -18px -18px 60px rgba(0, 0, 0, 0.25);
  }
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

@keyframes star-twinkle {
  0%, 100% { opacity: 0.3; transform: scale(1); }
  50% { opacity: 1; transform: scale(1.3); }
}

.star {
  position: absolute;
  background: var(--persona-gold-light);
  border-radius: 50%;
  animation: star-twinkle 3s ease-in-out infinite;
  z-index: 2;
}

.star::before {
  content: "";
  position: absolute;
  width: 100%;
  height: 100%;
  background: inherit;
  border-radius: inherit;
  transform: rotate(45deg);
}

.layout-container {
  z-index: 0;
  height: 100%;
  position: relative;
}

.star-field {
  z-index: 2;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.moon-container {
  z-index: 3;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.sakura-field {
  z-index: 5;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.sidebar {
  position: relative;
  transition: all 0.5s cubic-bezier(0.23, 1, 0.32, 1);
  overflow: hidden;
}

.sidebar::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(90deg,
    transparent,
    var(--persona-gold) 20%,
    var(--persona-gold-light) 50%,
    var(--persona-gold) 80%,
    transparent
  );
}

.sidebar-content {
  display: flex;
  flex-direction: column;
  height: 100%;
  padding: 16px 0;
}

.logo-area {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px 20px;
  margin-bottom: 24px;
}

.logo-icon-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.logo-icon {
  color: var(--persona-gold);
  filter: drop-shadow(0 0 10px rgba(212, 168, 75, 0.5));
  position: relative;
  z-index: 1;
}

.logo-glow {
  position: absolute;
  inset: -6px;
  background: radial-gradient(circle, rgba(212, 168, 75, 0.4) 0%, transparent 70%);
  border-radius: 50%;
  animation: breathe 3s ease-in-out infinite;
}

.logo-text-wrapper {
  overflow: hidden;
}

.logo-title {
  font-size: 20px;
  font-weight: 700;
  background: linear-gradient(135deg, var(--persona-gold), var(--persona-gold-light));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin: 0;
  line-height: 1.2;
  letter-spacing: 2px;
}

.logo-divider {
  width: 40px;
  height: 2px;
  background: linear-gradient(90deg, var(--persona-gold), transparent);
  margin: 6px 0;
}

.logo-subtitle {
  font-size: 10px;
  color: var(--persona-silver);
  letter-spacing: 1px;
  text-transform: uppercase;
}

.menu-section {
  flex: 1;
  overflow-y: auto;
  overflow-x: hidden;
}

.menu-title {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 0 20px;
  margin-bottom: 12px;
  font-size: 11px;
  font-weight: 600;
  color: var(--persona-silver);
  text-transform: uppercase;
  letter-spacing: 2px;
}

.nav-menu {
  display: flex;
  flex-direction: column;
  gap: 4px;
  padding: 0 12px;
}

.tarot-menu-item {
  position: relative;
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 14px 18px;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.23, 1, 0.32, 1);
  color: var(--persona-silver);
  overflow: hidden;
  border: 1px solid transparent;
  background: transparent;
}

.tarot-menu-item::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 2px;
  background: linear-gradient(90deg, transparent, var(--persona-gold), var(--persona-gold-light), var(--persona-gold), transparent);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.tarot-menu-item::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 2px;
  background: linear-gradient(90deg, transparent, var(--persona-gold), var(--persona-gold-light), var(--persona-gold), transparent);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.tarot-menu-item:hover {
  background: linear-gradient(135deg, rgba(212, 168, 75, 0.1), rgba(74, 63, 107, 0.15));
  color: var(--persona-gold-light);
  border-color: rgba(212, 168, 75, 0.25);
  transform: translateX(4px);
}

.tarot-menu-item:hover::before,
.tarot-menu-item:hover::after {
  opacity: 0.7;
}

.tarot-menu-item.active {
  background: linear-gradient(135deg, rgba(74, 63, 107, 0.4), rgba(212, 168, 75, 0.2));
  color: var(--persona-gold-light);
  border-color: rgba(212, 168, 75, 0.4);
  box-shadow:
    0 0 20px rgba(212, 168, 75, 0.15),
    inset 0 0 20px rgba(212, 168, 75, 0.05);
}

.tarot-menu-item.active::before,
.tarot-menu-item.active::after {
  opacity: 1;
}

.tarot-menu-item.collapsed {
  justify-content: center;
  padding: 14px;
}

.menu-item-text {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.menu-item-label {
  font-size: 14px;
  font-weight: 600;
  line-height: 1.2;
}

.menu-item-tarot {
  font-size: 10px;
  color: var(--persona-gold);
  text-transform: uppercase;
  letter-spacing: 2px;
  opacity: 0.7;
}

.menu-item-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  transition: transform 0.3s ease;
}

.menu-item:hover .menu-item-icon {
  transform: scale(1.1);
}

.menu-item-label {
  font-size: 14px;
  font-weight: 500;
  white-space: nowrap;
  letter-spacing: 0.5px;
}

.active-indicator {
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 3px;
  height: 60%;
  background: linear-gradient(180deg, var(--persona-gold), var(--persona-gold-light));
  border-radius: 0 2px 2px 0;
  box-shadow: 0 0 10px rgba(212, 168, 75, 0.5);
}

.sidebar-footer {
  padding: 12px;
  border-top: 1px solid rgba(212, 168, 75, 0.1);
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
}

.theme-toggle {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
  background: rgba(212, 168, 75, 0.1);
  border: 1px solid rgba(212, 168, 75, 0.2);
}

.theme-toggle:hover {
  background: rgba(212, 168, 75, 0.2);
  border-color: var(--persona-gold);
}

.theme-icon {
  color: var(--persona-gold);
  transition: all 0.3s ease;
}

.theme-icon.sunset {
  color: #f97316;
}

.theme-icon.day {
  color: #fbbf24;
}

.collapse-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border: none;
  border-radius: 4px;
  background: rgba(255, 255, 255, 0.05);
  color: var(--persona-silver);
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.collapse-btn:hover {
  background: rgba(212, 168, 75, 0.15);
  border-color: var(--persona-gold);
  color: var(--persona-gold);
}

.main-content {
  background: rgba(10, 10, 20, 0.6);
  backdrop-filter: blur(10px);
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 32px;
  height: 72px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.breadcrumb {
  display: flex;
  align-items: center;
  gap: 8px;
  color: var(--persona-silver);
  font-size: 14px;
}

.breadcrumb-icon {
  color: var(--persona-gold);
}

.breadcrumb-text {
  font-weight: 500;
}

.game-selector {
  --n-border: 1px solid rgba(212, 168, 75, 0.2) !important;
  --n-border-hover: 1px solid rgba(212, 168, 75, 0.5) !important;
  --n-border-focus: 1px solid var(--persona-gold) !important;
  --n-color: rgba(26, 26, 62, 0.8) !important;
  --n-color-hover: rgba(36, 36, 72, 0.9) !important;
  --n-color-focus: rgba(36, 36, 72, 1) !important;
  --n-text-color: var(--persona-gold-light) !important;
  --n-option-text-color: var(--persona-gold-light) !important;
  --n-option-color-hover: rgba(212, 168, 75, 0.2) !important;
  --n-border-radius: 4px !important;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.header-btn {
  width: 44px;
  height: 44px;
  color: var(--persona-silver) !important;
  transition: all 0.3s ease;
  border: 1px solid transparent;
  border-radius: 4px;
}

.header-btn:hover {
  color: var(--persona-gold) !important;
  background: rgba(212, 168, 75, 0.1) !important;
  border-color: rgba(212, 168, 75, 0.2);
}

.user-profile {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 6px 16px 6px 6px;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
  background: rgba(26, 26, 62, 0.6);
  border: 1px solid rgba(212, 168, 75, 0.15);
}

.user-profile:hover {
  background: rgba(74, 63, 107, 0.3);
  border-color: rgba(212, 168, 75, 0.4);
}

.avatar {
  background: linear-gradient(135deg, var(--persona-purple), var(--color-primary)) !important;
  border: 2px solid rgba(212, 168, 75, 0.3);
}

.user-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.username {
  font-size: 14px;
  font-weight: 600;
  color: var(--persona-gold-light);
  line-height: 1;
}

.user-level {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 11px;
  color: var(--persona-gold);
  line-height: 1;
}

.content {
  overflow-y: auto;
  height: calc(100vh - 72px);
}

.content-wrapper {
  padding: 24px 32px;
  max-width: 1600px;
  margin: 0 auto;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.moon-fade-enter-active,
.moon-fade-leave-active {
  transition: all 1s ease;
}

.moon-fade-enter-from,
.moon-fade-leave-to {
  opacity: 0;
  transform: scale(0.8);
}

::-webkit-scrollbar {
  width: 4px;
  height: 4px;
}

::-webkit-scrollbar-track {
  background: transparent;
}

::-webkit-scrollbar-thumb {
  background: linear-gradient(180deg, var(--persona-gold), var(--persona-purple));
  border-radius: 2px;
}

::-webkit-scrollbar-thumb:hover {
  background: linear-gradient(180deg, var(--persona-gold-light), var(--color-primary));
}
</style>

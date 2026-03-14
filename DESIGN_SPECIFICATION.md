# DominionCore — Full Design Specification

## Overview

DominionCore is a Minecraft mod built on Forge/Fabric (targeting modern MC versions) that transforms the base game into a living, breathing MMO-style power progression experience.

Every system is interconnected:
- Your **bloodline** shapes your identity.
- Your **dominion** defines how your power grows.
- Your **faction** determines your authority.
- Your **religion** defines your influence.

Nothing is static. Power always scales. The world always changes.

DominionCore is designed as a **framework first, content second** system. Every major system is exposed to JSON configuration and a built-in scripting language, so servers and addon developers can extend it without modifying Java code.

---

## 1) Bloodlines

### What They Are
Bloodlines are your core identity. They function like deep RPG classes that can be upgraded, mutated, prestiged, and eventually combined in late game.

Players start with no bloodline and must choose one. Bloodlines are permanent by default (configurable).

### Core Structure
Each bloodline includes:
- Primary resource bar (e.g., Blood, Essence, Aura, Souls, Faith)
- Passive abilities tied to resource state
- Active abilities tied to keybind use
- Scaling condition (kills, land, time alive, followers, etc.)
- 5-tier prestige track
- Mutation system that permanently alters ability behavior

Bloodlines are data-driven via JSON and are extensible via addon packs or DominionScript.

### Bloodline Selection Screen
- Full-screen custom GUI (not chest/inventory GUI)
- Animated theme effects per bloodline
- Left: bloodline card list (name, icon, difficulty, flavor text)
- Center: rotating 3D character preview
- Right: detailed stats, abilities, scaling, strengths/weaknesses
- Locked entries are greyed with unlock tooltips
- Confirmation popup on final selection

### Bloodline Upgrade Tree GUI
- Full-screen branching tree with glowing node connections
- Hexagonal nodes with lock/unlock visual states
- Hover side panel with formulas and requirements
- Right-click ability preview panel
- Left-click unlock flow with point spend + visual feedback
- Prestige milestones branch into mutation paths
- Supports smooth zoom/scroll + top search filter

---

## 2) Dominions

### What They Are
Dominions are the scaling engine of player power progression.

- One Primary Dominion by default
- Secondary slot unlockable later
- Switching is possible with cooldown
- Maintenance failures can weaken/remove dominion effects

### Dominion Types

#### Blood Dominion
- Scales from combat activity: kills, damage dealt, damage survived
- Generates Blood Power
- Improves damage, speed, ability potency
- Risk mechanic: death penalties at high thresholds

#### Authority Dominion
- Scales from leadership and territory control
- Formula:
  - `Authority = (Members × 2) + (ClaimedChunks × 3) + (OnlineFollowers × 5) + (Structures × 4)`
- Grants defense, command tools, territorial aura bonuses

#### Faith Dominion
- Scales from active worship infrastructure and follower participation
- Inputs: followers, temples, rituals, prayer frequency
- Unlocks miracle-tier power effects

#### Corruption Dominion
- Scales from soul harvesting and land corruption spread
- Corrupted land returns passive power
- Supports buff theft and corruption-themed world visuals

#### Begging Dominion
- Scales only from voluntary donations
- No combat or territory scaling contribution
- Unlocks social-manipulation style utility at high power

### Acquisition Paths
- Starter GUI selection
- Achievement unlocks
- Ritual structure completion
- Faction rank promotions
- Deity bestowal

### Dominion Manager GUI
- Current dominion and animated icon
- Live formula breakdown with substituted values
- Secondary slot status
- Switch cooldown visualization
- Historical power graph
- Contribution condition breakdown

---

## 3) Factions

### Role in System
Factions are a core integrated system that directly affects dominion scaling, territory combat, and religious conflict structures.

### Core Features
- Configurable creation cost
- Chunk claims with configurable protection rules
- Rank hierarchy with configurable permissions
- War declaration and contested-territory logic
- Treasury + deposits + configurable automated tax cycles
- Territory buffs/debuffs based on ownership alignment
- Live faction power score feeding authority formulas

### Faction GUI Tabs
- **Members**: rank, status, dominion, contribution
- **Territory**: interactive chunk map with ownership overlays
- **Wars**: ongoing conflicts, objectives, score tracking, peace/declare actions
- **Upgrades**: permanent faction improvements
- **Treasury**: balances, tax history, transaction logs

---

## 4) Religion

### What They Are
Religions are player-created influence systems with deity leadership and faith-based scaling.

### Core Mechanics
- Players create religion identity (name/icon/commandments/holy area)
- Creator becomes deity
- Followers join voluntarily
- Faith generation sources:
  - follower online presence
  - `/pray` activity
  - temples and altars
  - ritual completion
- Tiered miracle unlock progression
- Holy land claims with ally/enemy effect differences
- Multi-religion coexistence, alliance, and conflict support
- Faith decay and collapse risk when deity is inactive or influence drops

### Miracle Tiers
- **Tier 1**: movement/regen/prayer-response utility
- **Tier 2**: area healing + protection dome
- **Tier 3**: smite + weather manipulation
- **Tier 4**: limited resurrection
- **Tier 5**: global-scale world event triggers

### Religion GUI Sections
- Overview
- Followers
- Blessings
- Holy Structures
- Miracles
- Commandments

---

## 5) DominionScript (Scripting Engine)

### Purpose
DominionScript is a hot-reloadable built-in language for defining custom mechanics without Java source changes.

### Script Location
- `/dominionscripts/`
- Reload command: `/dominion reload`

### Example
```txt
dominion "ShadowWalker":
    resource: "shadow_essence"
    scaling:
        on kill:
            add 10 shadow_essence
        on sneak_time > 5s:
            add 2 shadow_essence per second
    passive:
        if shadow_essence > 50:
            apply invisibility 1 to self
        if shadow_essence > 150:
            apply speed 2 to self
    active "Void Step":
        cost: 30 shadow_essence
        on use:
            teleport self to cursor_target
            apply blindness 3 to nearby_enemies radius 5
```

### Script Scope
Can define:
- Dominions
- Bloodlines and ability trees
- Resources
- GUI entries
- Events/triggers
- PvP scaling rules
- Faction rank/permission structures
- Religion miracles
- World events

### Hook Coverage
Includes combat, territory, faction, religion, ritual, chunk, time, biome, health, item, ability, and world-event hooks, plus custom trigger support.

---

## 6) GUI Framework

### Design Philosophy
No chest-style interfaces. All systems use a custom-rendered panel framework.

### Visual Standards
- Darkened/blurred world background
- Rounded dark panels with subtle glow
- Smooth transitions and micro-interactions
- Color-coded typography
- 32x32 custom icon standards
- Keybind hints
- Search and pagination for larger collections
- Toggle switches in settings contexts

### Planned GUI Screens
1. Bloodline Selection
2. Bloodline Upgrade Tree
3. Dominion Manager
4. Ability Hotbar Config
5. Faction Menu
6. Religion Menu
7. Territory Map
8. World Events Panel
9. Prestige Screen
10. Admin Control Panel
11. Player Profile Viewer

---

## 7) PvP Scaling

### Principles
Combat is always context-sensitive and influenced by player progression systems.

### Systems
- Kill score scaling with configurable soft cap
- Territory ownership combat modifiers
- Anti-one-shot protection system
- Dynamic armor resistance learning by absorbed damage types
- Boss-tier threshold status with kill bounties/bonuses/announcements
- Weapon kill tracking + affinity scaling + visual evolution

---

## 8) World Events

### Trigger Sources
- Time/day cycle
- Max-tier deity miracle
- Admin command
- DominionScript `trigger_event`
- Emergent world-state conditions

### Example Events
- Blood Moon
- Holy War
- Corruption Surge
- Divine Trial
- Faction Siege

All active events are reflected in HUD banners with timers.

---

## 9) HUD

Custom HUD replaces/augments vanilla UI where relevant.

### Elements
- Bloodline resource bar
- Dominion power indicator
- 4-slot active ability display with cooldown overlays
- Territory ownership + effect indicator
- World event banner (expand/collapse behavior)
- Optional faction trend bar
- Repositionable via `/dominion hud`

---

## 10) Configuration & Admin System

Everything is configurable; no hidden balancing constants should remain hardcoded.

### Generated Config Tree
- `core.json`
- `scaling.json`
- `bloodlines/`
- `dominions/`
- `factions.json`
- `religion.json`
- `pvp.json`
- `gui.json`
- `events.json`

### Admin Control Panel Capabilities
- Live player-state overview
- Manual power adjustments
- Runtime system toggles
- World-event broadcasting
- Bloodline/dominion bans
- Script error visibility + reload control
- Real-time scaling cap adjustments

---

## 11) Late Game & Ascension

Ascension is a one-time irreversible high-tier transformation (admin-resettable).

### Ascension Outcomes
- World Boss Mode
- Personal pocket dimension control
- Regional/global weather control
- Time/sky event manipulation
- Faction-wide divine territory state
- Direct world-event trigger access

Ascended players are globally visible with special identity styling and map indicators.

---

## 12) Addon Architecture

DominionCore exposes an API jar for external mod integration.

Addon developers can:
- Register bloodlines/dominions
- Register extension GUI panels
- Add miracle/world-event types
- Subscribe to DominionCore events
- Extend DominionScript syntax

JSON-only addon packs may provide content expansions without Java code:
- Bloodlines
- Dominions
- Abilities
- GUI icons
- Event configs
- Script templates

---

## Summary
DominionCore is intended to be a long-term foundation for scalable, social, and systemic Minecraft progression.

It emphasizes:
- meaningful action-to-power loops,
- modern custom GUI standards,
- fully configurable server-side control,
- and extensibility via scripting + addon APIs.

It is designed as a platform, not a closed feature set.

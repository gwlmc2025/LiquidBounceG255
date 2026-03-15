<script lang="ts">
    import {createEventDispatcher} from "svelte";
    import type {ConfigurableSetting, ModuleSetting,} from "../../../integration/types";
    import GenericSetting from "./common/GenericSetting.svelte";
    import ExpandArrow from "./common/ExpandArrow.svelte";
    import {setItem} from "../../../integration/persistent_storage";
    import {convertToSpacedString, spaceSeperatedNames} from "../../../theme/theme_config";

    export let setting: ModuleSetting;
    export let path: string;
    export let hideExpandControl: boolean = false;

    const cSetting = setting as ConfigurableSetting;
    const thisPath = `${path}.${cSetting.name}`;

    const dispatch = createEventDispatcher();

    function handleChange() {
        setting = {...cSetting};
        dispatch("change");
    }

    let expanded = hideExpandControl ? true : localStorage.getItem(thisPath) === "true";

    $: setItem(thisPath, expanded.toString());

    function toggleExpanded() {
        if (hideExpandControl) {
            return;
        }
        expanded = !expanded;
    }

    // 查找翻译
    function findTranslation(name: string): string {
        // 从路径中提取模块名称
        const pathParts = path.split('.');
        const moduleName = pathParts[1]; // liquidbounce.module.xxx.value
        
        // 构建翻译键
        const translationKey = `liquidbounce.module.${moduleName}.value.${name}.name`;
        
        // 查找翻译
        const translations = (window as any).translations || {};
        return translations[translationKey] || ($spaceSeperatedNames ? convertToSpacedString(name) : name);
    }
</script>

<div class="setting">
    <!-- svelte-ignore a11y-no-static-element-interactions -->
    <div class="head" class:expanded on:contextmenu|preventDefault={toggleExpanded}>
        <div class="title">{findTranslation(setting.name)}</div>
        {#if !hideExpandControl}
            <ExpandArrow bind:expanded />
        {/if}
    </div>

    {#if expanded}
        <div class="nested-settings">
            {#each cSetting.value as setting (setting.name)}
                <GenericSetting path={thisPath} bind:setting on:change={handleChange}/>
            {/each}
        </div>
    {/if}
</div>

<style lang="scss">
  @use "../../../colors.scss" as *;

  .setting {
    padding: 7px 0;
  }

  .title {
    color: $clickgui-text-color;
    font-size: 12px;
    font-weight: 600;
  }

  .head {
    display: flex;
    justify-content: space-between;
    transition: ease margin-bottom .2s;

    &.expanded {
      margin-bottom: 10px;
    }
  }

  .nested-settings {
    border-left: solid 2px $accent-color;
    padding-left: 7px;
  }
</style>

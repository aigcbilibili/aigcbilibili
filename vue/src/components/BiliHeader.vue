<template>
    <!--景深背景图-->
    <div class="biliBg">
        <div class="animated-banner" @mouseover="moveIn()" @mouseleave="moveOut()">
            <div class="layer">
                <img src="@/assets/biliBg/01.png" 
                data-height="180" data-width="1920" height="162" width="1728" style="height: 162px; width: 1728px; transform: translate(0px, 0px) rotate(0deg) scale(1); opacity: 1; filter: blur(10px);">
            </div>
            <div class="layer">
                <img src="@/assets/biliBg/02.png" 
                data-height="180" data-width="1920" height="162" width="1728" style="height: 162px; width: 1728px; transform: translate(0px, -9px) rotate(0deg) scale(1); opacity: 1; filter: blur(5px);">
            </div>
            <div class="layer">
                <img src="@/assets/biliBg/03.png" 
                data-height="164" data-width="1920" height="147" width="1728" style="height: 147.6px; width: 1728px; transform: translate(0px, 0px) rotate(0deg) scale(1); opacity: 1;">
            </div>
            <div class="layer">
                <img src="@/assets/biliBg/04.png" 
                data-height="76" data-width="826" height="76" width="826" style="height: 76px; width: 826px; transform: translate(0px, -40px) rotate(0deg) scale(1); opacity: 1;">
            </div>
            <div class="layer">
                <img src="@/assets/biliBg/05.png" 
                data-height="180" data-width="1523" height="180" width="1523" style="height: 180px; width: 1523px; transform: translate(100px, 0px) rotate(0deg) scale(1); opacity: 1;">
            </div>
            <div class="layer">
                <img src="@/assets/biliBg/06.png" 
                data-height="74" data-width="424" height="66" width="381" style="height: 66.6px; width: 381.6px; transform: translate(-126px, -45px) rotate(0deg) scale(1); opacity: 1;">
            </div>
            <div class="layer">
                <img src="@/assets/biliBg/07.png" 
                data-height="156" data-width="1920" height="156" width="1920" style="height: 156px; width: 1920px; transform: translate(0px, 10px) rotate(0deg) scale(1); opacity: 1;">
            </div>
            <div class="layer">
                <img src="@/assets/biliBg/08.png" 
                data-height="109" data-width="104" height="130" width="124" style="height: 130.8px; width: 124.8px; transform: translate(276px, -48px) rotate(0deg) scale(1); opacity: 1;">
            </div>
            <div class="layer">
                <img src="@/assets/biliBg/09.png" 
                data-height="180" data-width="589" height="180" width="589" style="height: 180px; width: 589px; transform: translate(660px, 0px) rotate(0deg) scale(1); opacity: 1;">
            </div>
            <div class="layer">
                <img src="@/assets/biliBg/10.png" 
                data-height="157" data-width="420" height="157" width="420" style="height: 157px; width: 420px; transform: translate(750px, 0px) rotate(0deg) scale(1); opacity: 1; filter: blur(0);">
            </div>
            <div class="layer">
                <img src="@/assets/biliBg/11.png" 
                data-height="149" data-width="389" height="141" width="369" style="height: 141.55px; width: 369.55px; transform: translate(503.5px, 14.25px) rotate(0deg) scale(1); opacity: 1;">
            </div>
            <div class="layer">
                <img src="@/assets/biliBg/12.png" 
                data-height="148" data-width="191" height="133" width="171" style="height: 133.2px; width: 171.9px; transform: translate(504px, 13.5px) rotate(0deg) scale(1); opacity: 1;">
            </div>
            <div class="layer">
                <img src="@/assets/biliBg/13.png" 
                data-height="166" data-width="226" height="149" width="203" style="height: 149.4px; width: 203.4px; transform: translate(-171px, -4.5px) rotate(0deg) scale(1); opacity: 1;">
            </div>
            <div class="layer">
                <img src="@/assets/biliBg/14.png" 
                data-height="180" data-width="813" height="162" width="731" style="height: 162px; width: 731.7px; transform: translate(-540px, 0px) rotate(0deg) scale(1); opacity: 1;">
            </div>
            <div class="layer">
                <img src="@/assets/biliBg/15.png" 
                data-height="118" data-width="307" height="112" width="291" style="height: 112.1px; width: 291.65px; transform: translate(-332.5px, 23.75px) rotate(0deg) scale(1); opacity: 1;">
            </div>
            <div class="layer">
                <img src="@/assets/biliBg/16.png" 
                data-height="122" data-width="201" height="115" width="190" style="height: 115.9px; width: 190.95px; transform: translate(-551px, 23.75px) rotate(0deg) scale(1); opacity: 1;">
            </div>
            <div class="layer">
                <img src="@/assets/biliBg/17.png" 
                data-height="180" data-width="474" height="144" width="379" style="height: 144px; width: 379.2px; transform: translate(-880px, 16px) rotate(0deg) scale(1); opacity: 1;">
            </div>
            <div class="layer">
                <img src="@/assets/biliBg/18.png" 
                data-height="145" data-width="288" height="145" width="288" style="height: 145px; width: 288px; transform: translate(20px, 10px) rotate(0deg) scale(1); opacity: 0.3;">
            </div>
            <div class="layer">
                <video loop src="@/assets/biliBg/snow01.webm" 
                playsinline="" width="2112" height="154" style="object-fit: cover; height: 154px; width: 2112px; transform: translate(0px, 0px) rotate(0deg) scale(1); opacity: 1;" data-height="140" data-width="1920">
                </video>
            </div>
            <div class="layer">
                <video loop src="@/assets/biliBg/fire01.webm" 
                playsinline="" width="420" height="630" style="object-fit: cover; height: 630px; width: 420px; transform: translate(42px, -84px) rotate(0deg) scale(1); filter: blur(5px); opacity: 0.3;" data-height="300" data-width="200">
                </video>
            </div>
        </div>
    </div>
</template>

<script setup>
// 
const moveIn = () => {

}
// 
const moveOut = () => {

}
</script>

<style lang="scss" scoped>
@mixin base-bg {
    left: 0;
    top: 0;
}
.biliBg {
    position: relative;
    z-index: 0;
    display: flex;
    -ms-flex-pack: center;
    justify-content: center;
    margin: 0 auto;
    box-sizing: block;
    min-width: 704px;
    min-height: 155px;
    height: 20vh;
    max-height: 240px;
    background-size: cover;
    background-repeat: no-repeat;
}
.animated-banner {
    @include base-bg;
    position: absolute;
    bottom: 0;
    right: 0;
    overflow: hidden;
   
}
.layer {
    @include base-bg;
    position: absolute;
    height: 100%;
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
}
</style>
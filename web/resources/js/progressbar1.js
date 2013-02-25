var stage
function initVisualization(){
    stage = new Kinetic.Stage({
        container: 'container',
        width: 1100,
        height: 500
    });

    var barsLayer = new Kinetic.Layer();
    var worldcatLayer = new Kinetic.Layer();
    var arxivLayer = new Kinetic.Layer();
    var mendeleyLayer = new Kinetic.Layer();
    var nytLayer = new Kinetic.Layer();

    var arxivRect = new Kinetic.Rect({
        x: 10,
        y: stage.getHeight() - 115 - 10,
        width: 245,
        height: 1,
        fill: 'green',
        //        stroke: 'black',
        //        strokeWidth: 0,
        name:'arxivBar'
    });

    var mendeleyRect = new Kinetic.Rect({
        x: 10 + 245 + 10,
        y: stage.getHeight() - 115 - 10,
        width: 245,
        height: 1,
        fill: 'green',
        //        stroke: 'black',
        //        strokeWidth: 0,
        name:'mendeleyBar'
    });

    var worldcatRect = new Kinetic.Rect({
        x: 10 + 245 + 10 + 245 + 10,
        y: stage.getHeight() - 115 - 10,
        width: 245,
        height: 1,
        fill: 'green',
        //        stroke: 'black',
        //        strokeWidth: 0,
        name:'worldcatBar'
    });

    var nytRect = new Kinetic.Rect({
        x: 10 + 245 + 10 + 245 + 10 + 245 + 10,
        y: stage.getHeight() - 115 - 10,
        width: 245,
        height: 1,
        fill: 'green',
        //        stroke: 'black',
        //        strokeWidth: 0,
        name:'nytBar'
    });

    barsLayer.add(arxivRect);
    barsLayer.add(mendeleyRect);
    barsLayer.add(worldcatRect);
    barsLayer.add(nytRect);
    stage.add(barsLayer);

    
    var arxivPic = new Image();
    arxivPic.onload = function() {
        var arxivPicOriginal = new Kinetic.Image({
            x: 10,
            y: stage.getHeight() - 5 - 115,
            image: arxivPic,
            width: 245,
            height: 115

        });
        arxivLayer.add(arxivPicOriginal);
        stage.add(arxivLayer);

    }
    arxivPic.src = 'resources/img/arxiv.jpg';

 
    var mendeleyPic = new Image();
    mendeleyPic.onload = function() {
        var mendeleyPicOriginal = new Kinetic.Image({
            x: 10 + 245 + 10,
            y: stage.getHeight() - 5 - 115,
            image: mendeleyPic,
            width: 245,
            height: 115

        });
        mendeleyLayer.add(mendeleyPicOriginal);
        stage.add(mendeleyLayer);
    }
    mendeleyPic.src = 'resources/img/mendeley.jpg';
 
    var worldcatPic = new Image();
    worldcatPic.onload = function() {
        var worldcatPicOriginal = new Kinetic.Image({
            x: 10 + 245 + 10 + 245 + 10,
            y: stage.getHeight() - 5 - 115,
            image: worldcatPic,
            width: 245,
            height: 115

        });
        worldcatLayer.add(worldcatPicOriginal);
        stage.add(worldcatLayer);
        var worldcatz = new Image();

        worldcatPicOriginal.on("mouseover", function() {
            worldcatPicOriginal.setImage(worldcatz);
            worldcatLayer.draw();
        });
        worldcatPicOriginal.on("mouseout", function() {
            worldcatPicOriginal.setImage(worldcatPic);
            worldcatLayer.draw();
        });
        worldcatz.src = 'resources/img/worldcatz.jpg';

    }
    worldcatPic.src = 'resources/img/worldcat.jpg';

 
    var nytPic = new Image();
    nytPic.onload = function() {
        var nytPicOriginal = new Kinetic.Image({
            x: 10 + 245 + 10 + 245 + 10 + 245 + 10,
            y: stage.getHeight() - 5 - 115,
            image: nytPic,
            width: 245,
            height: 115

        });
        nytLayer.add(nytPicOriginal);
        stage.add(nytLayer);
    }
    nytPic.src = 'resources/img/nyt.jpg';
 
}



function updateVisualization(){
//    var progressMsg = document.getElementById('formID:counter').value;
    var progressMsg = $('#counter').val();
    console.log("value of progressMsg is: "+progressMsg);
    var worldcat;
    if (progressMsg.indexOf("worldcat in progress")!=-1){
        worldcat = stage.get('.worldcatBar');
        worldcat.apply('transitionTo', {
            scale: {
                y: -100
            },
            duration: 2,
            easing: 'elastic-ease-out'
        });
    }
    if (progressMsg.indexOf("worldcat returned")!=-1){
        worldcat = stage.get('.worldcatBar');
        worldcat.apply('transitionTo', {
            scale: {
                y: -200
            },
            duration: 2,
            easing: 'elastic-ease-out'
        });
    }
    if (progressMsg.indexOf("mendeley returned")!=-1){
        var mendeley = stage.get('.mendeleyBar');
        mendeley.apply('transitionTo', {
            scale: {
                y: -200
            },
            duration: 2,
            easing: 'elastic-ease-out'
        });
    }

    if (progressMsg.indexOf("arxiv returned")!=-1){
        var arxiv = stage.get('.arxivBar');
        arxiv.apply('transitionTo', {
            scale: {
                y: -200
            },
            duration: 2,
            easing: 'elastic-ease-out'
        });
    }

    if (progressMsg.indexOf("nyt returned")!=-1){
        var nyt = stage.get('.nytBar');
        nyt.apply('transitionTo', {
            scale: {
                y: -200
            },
            duration: 2,
            easing: 'elastic-ease-out'
        });
    }

}
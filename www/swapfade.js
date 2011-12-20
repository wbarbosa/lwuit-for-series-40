// ISF1.11 :: Image swap-fade 
// *****************************************************
// DOM scripting by brothercake -- http://www.brothercake.com/
//******************************************************
//global object
var isf = { 'clock' : null, 'fade' : true, 'count' : 1 }
/*******************************************************



/*****************************************************************************
 List the images that need to be cached
*****************************************************************************/

isf.imgs = [];
//[
//	'buttons/udm4-whitebutton88x31.gif',
//	'buttons/udm4-greenbutton88x31.gif',
//	'buttons/udm4-purplebutton88x31.gif'
//	];

/*****************************************************************************
*****************************************************************************/



//cache the images
//isf.imgsLen = isf.imgs.length;
//isf.cache = [];
//for(var i=0; i<isf.imgsLen; i++)
//{
//	isf.cache[i] = new Image;
//	isf.cache[i].src = isf.imgs[i];
//}


//swapfade setup function
function swapfade()
{

	//if the timer is not already going
	if(isf.clock == null)
	{	
		//copy the image object 
		isf.obj = document.getElementById(arguments[0]);
		  if (isf.obj == '')
  isf.obj = document.getElementsByName(arguments[0])[1];
		//copy the image src argument 
		isf.src = arguments[1];
		//store the supported form of opacity	
		
		if(typeof isf.obj.style.opacity != 'undefined')
		{
			isf.type = 'w3c';
		}
		else if(typeof isf.obj.style.MozOpacity != 'undefined')
		{
		isf.type = 'moz';
		}
		else if(typeof isf.obj.style.KhtmlOpacity != 'undefined')
		{
		isf.type = 'khtml';
		}
		else if(typeof isf.obj.filters == 'object')
		{
			//weed out win/ie5.0 by testing the length of the filters collection (where filters is an object with no data)
			//then weed out mac/ie5 by testing first the existence of the alpha object (to prevent errors in win/ie5.0)
			//then the returned value type, which should be a number, but in mac/ie5 is an empty string
			isf.type = (isf.obj.filters.length > 0 && typeof isf.obj.filters.alpha == 'object' && typeof isf.obj.filters.alpha.opacity == 'number') ? 'ie' : 'none';
		}
		else
		{
			isf.type = 'none';
		}
		
		//change the image alt text if defined
		if(typeof arguments[3] != 'undefined' && arguments[3] != '')
		{
			isf.obj.alt = arguments[3];
		}
		
		//if any kind of opacity is supported
		if(isf.type != 'none')
		{
		
			//copy and convert fade duration argument 
			//the duration specifies the whole transition
			//but the swapfade is two distinct transitions
			isf.length = parseInt(arguments[2], 10) * 100;
			
			//create fade resolution argument as 20 steps per transition
			//again, split for the two distrinct transitions
			isf.resolution = parseInt(arguments[2], 10) * 10;
			
			//start the timer
			isf.clock = setInterval('isf.swapfade()', isf.length/isf.resolution);
			
		}
				
		//otherwise if opacity is not supported
		else
		{
			//just do the image swap
			isf.obj.src = isf.src;
		}
		
	}
};


//swapfade timer function
isf.swapfade = function()
{
	//increase or reduce the counter on an exponential scale
	isf.count = (isf.fade) ? isf.count * 0.9 : (isf.count * (1/0.9)); 
	
	//if the counter has reached the bottom
	if(isf.count < (1 / isf.resolution))
	{
		//clear the timer
		clearInterval(isf.clock);
		isf.clock = null;

		//do the image swap
		isf.obj.src = isf.src;

		//reverse the fade direction flag
		isf.fade = false;
		
		//restart the timer
		isf.clock = setInterval('isf.swapfade()', isf.length/isf.resolution);

	}
	
	//if the counter has reached the top
	if(isf.count > (1 - (1 / isf.resolution)))
	{
		//clear the timer
		clearInterval(isf.clock);
		isf.clock = null;

		//reset the fade direction flag
		isf.fade = true;
		
		//reset the counter
		isf.count = 1;
	}

	//set new opacity value on element
	//using whatever method is supported
	switch(isf.type)
	{
		case 'ie' :
			isf.obj.filters.alpha.opacity = isf.count * 100;
			break;
			
		case 'khtml' :
			isf.obj.style.KhtmlOpacity = isf.count;
			break;
			
		case 'moz' : 
			//restrict max opacity to prevent a visual popping effect in firefox
			isf.obj.style.MozOpacity = (isf.count == 1 ? 0.9999999 : isf.count);
			break;
			
		default : 
			//restrict max opacity to prevent a visual popping effect in firefox
			isf.obj.style.opacity = (isf.count == 1 ? 0.9999999 : isf.count);
	}
};



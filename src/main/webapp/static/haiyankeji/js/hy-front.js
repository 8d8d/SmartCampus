// JavaScript Document
(function(){
	if(document.getElementById("idx-banner")); {
		var banner = document.getElementById("idx-banner");
		if(banner != null) {
			banner.style.marginLeft = - banner.width / 2 +"px";
		}
	}
})();

function tab(tid,cid,con){
	$('.'+tid).find('li').each(function(i){
		$(this).click(function(){
			$(this).siblings().removeClass('cur').end().addClass('cur');
			$('.'+cid).find(con).eq(i).siblings().hide().end().show();
		});
	}).eq(0).trigger('click');
}; 
// Wait for the document to finish loading
document.addEventListener("DOMContentLoaded", function() {
  // Get the loading text container
  var loadingText = document.querySelector(".loading-text");

  // Animate the loading text
  TweenMax.fromTo(
    loadingText,
    0.5,
    { opacity: 0, y: -20 },
    { opacity: 1, y: 0, delay: 0.5 }
  );

  // Get the SVG paths
  var path1 = document.querySelector("#path1");
  var path2 = document.querySelector("#path2");

  // Animate the SVG paths
  var tl = new TimelineMax({ repeat: -1, yoyo: true });
  tl.to(path1, 2, { morphSVG: "#path2", ease: Power1.easeInOut });
  tl.to(path1, 2, { morphSVG: "#path1", ease: Power1.easeInOut });
});


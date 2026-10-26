// Ashok Jewellers public catalogue — demo data until connected to the owner app.
const SHOP = {
  phone: "+910000000000", // TODO: replace with the shop's phone number
  whatsapp: "910000000000", // digits only, country code included; replace before launch
  address: "Add Ashok Jewellers' full shop address here",
  hours: "Please confirm opening hours with the shop",
  mapsUrl: "", // paste the shop's Google Maps share URL
  rates: { gold22: 0, gold24: 0, silver: 0, updatedAt: "Demo rates — confirm with shop" }
};

const PRODUCTS = [
  {id:"GOL-NCK-001",name:{en:"Temple Necklace",kn:"ದೇವಾಲಯ ಶೈಲಿಯ ಹಾರ"},category:"Necklaces",metal:"Gold",purity:"22K",weight:"24.80 g",description:{en:"Traditional temple-inspired design.",kn:"ಸಾಂಪ್ರದಾಯಿಕ ದೇವಾಲಯ ಶೈಲಿಯ ವಿನ್ಯಾಸ."},available:true,image:"https://images.unsplash.com/photo-1599643478518-a784e5dc4c8f?auto=format&fit=crop&w=900&q=80"},
  {id:"GOL-BNG-001",name:{en:"Classic Gold Bangles",kn:"ಸಾಂಪ್ರದಾಯಿಕ ಚಿನ್ನದ ಬಳೆಗಳು"},category:"Bangles",metal:"Gold",purity:"22K",weight:"18.20 g",description:{en:"Elegant bangles for festive occasions.",kn:"ಹಬ್ಬದ ಸಂದರ್ಭಗಳಿಗೆ ಸುಂದರವಾದ ಬಳೆಗಳು."},available:true,image:"https://images.unsplash.com/photo-1611652022419-a9419f74343d?auto=format&fit=crop&w=900&q=80"},
  {id:"GOL-ERR-001",name:{en:"Traditional Earrings",kn:"ಸಾಂಪ್ರದಾಯಿಕ ಕಿವಿಯೋಲೆಗಳು"},category:"Earrings",metal:"Gold",purity:"22K",weight:"8.40 g",description:{en:"A classic addition to your collection.",kn:"ನಿಮ್ಮ ಸಂಗ್ರಹಕ್ಕೆ ಸೊಗಸಾದ ಸೇರ್ಪಡೆ."},available:false,image:"https://images.unsplash.com/photo-1630019852942-f89202989a59?auto=format&fit=crop&w=900&q=80"},
  {id:"SLV-ANK-001",name:{en:"Silver Anklets",kn:"ಬೆಳ್ಳಿಯ ಕಾಲ್ಗೆಜ್ಜೆಗಳು"},category:"Silver",metal:"Silver",purity:"Silver",weight:"32.00 g",description:{en:"Beautiful silver anklets.",kn:"ಸುಂದರವಾದ ಬೆಳ್ಳಿಯ ಕಾಲ್ಗೆಜ್ಜೆಗಳು."},available:true,image:"https://images.unsplash.com/photo-1617038220319-276d3cfab638?auto=format&fit=crop&w=900&q=80"},
  {id:"GOL-RNG-001",name:{en:"Gold Ring",kn:"ಚಿನ್ನದ ಉಂಗುರ"},category:"Rings",metal:"Gold",purity:"22K",weight:"5.10 g",description:{en:"A refined everyday design.",kn:"ದೈನಂದಿನ ಬಳಕೆಗೆ ಸೊಗಸಾದ ವಿನ್ಯಾಸ."},available:true,image:"https://images.unsplash.com/photo-1605100804763-247f67b3557e?auto=format&fit=crop&w=900&q=80"},
  {id:"SLV-CHN-001",name:{en:"Silver Chain",kn:"ಬೆಳ್ಳಿಯ ಸರ"},category:"Chains",metal:"Silver",purity:"Silver",weight:"16.50 g",description:{en:"Simple and timeless silver chain.",kn:"ಸರಳ ಮತ್ತು ಕಾಲಾತೀತ ಬೆಳ್ಳಿಯ ಸರ."},available:true,image:"https://images.unsplash.com/photo-1611591437281-460bfbe1220a?auto=format&fit=crop&w=900&q=80"}
];

const BANNERS = [
  {title:{en:"Jewellery for Every Occasion",kn:"ಪ್ರತಿ ಸಂದರ್ಭಕ್ಕೂ ಆಭರಣಗಳು"},text:{en:"Discover designs for life's most memorable moments.",kn:"ಜೀವನದ ವಿಶೇಷ ಕ್ಷಣಗಳಿಗೆ ಸುಂದರ ವಿನ್ಯಾಸಗಳನ್ನು ಅನ್ವೇಷಿಸಿ."},eyebrow:{en:"TRADITION · TRUST · TIMELESS BEAUTY",kn:"ಸಂಪ್ರದಾಯ · ನಂಬಿಕೆ · ಕಾಲಾತೀತ ಸೌಂದರ್ಯ"},image:"https://images.unsplash.com/photo-1617038220319-276d3cfab638?auto=format&fit=crop&w=1400&q=85"},
  {title:{en:"Celebrate Every Moment",kn:"ಪ್ರತಿ ಕ್ಷಣವನ್ನೂ ಸಂಭ್ರಮಿಸಿ"},text:{en:"Explore traditional details and timeless designs.",kn:"ಸಾಂಪ್ರದಾಯಿಕ ವೈಶಿಷ್ಟ್ಯಗಳು ಮತ್ತು ಸುಂದರ ವಿನ್ಯಾಸಗಳನ್ನು ನೋಡಿ."},eyebrow:{en:"A COLLECTION TO CHERISH",kn:"ನೆನಪಿನಲ್ಲಿ ಉಳಿಯುವ ಸಂಗ್ರಹ"},image:"https://images.unsplash.com/photo-1599643478518-a784e5dc4c8f?auto=format&fit=crop&w=1400&q=85"},
  {title:{en:"Elegance in Every Detail",kn:"ಪ್ರತಿ ವಿವರದಲ್ಲೂ ಸೊಬಗು"},text:{en:"Browse our catalogue and ask us about a design.",kn:"ನಮ್ಮ ವಿನ್ಯಾಸಗಳನ್ನು ನೋಡಿ, ವಿವರಗಳಿಗಾಗಿ ಸಂಪರ್ಕಿಸಿ."},eyebrow:{en:"CRAFTING ELEGANCE IN GOLD",kn:"ಚಿನ್ನದಲ್ಲಿ ಸೊಬಗಿನ ವಿನ್ಯಾಸ"},image:"https://images.unsplash.com/photo-1611652022419-a9419f74343d?auto=format&fit=crop&w=1400&q=85"}
];

const I18N = {
  en:{collections:"Collections",rates:"Metal rates",visit:"Visit us",eyebrow:"TRADITION · TRUST · TIMELESS BEAUTY",heroText:"Discover designs for life's most memorable moments.",explore:"Explore collections",dailyRates:"DAILY RATES",metalRates:"Today's metal rates",gold22:"22K Gold",gold24:"24K Gold",silver:"Silver",perGram:"per gram",rateNote:"Rates are entered by the shop and may change. Please confirm before visiting.",curated:"CURATED FOR YOU",ourDesigns:"Explore our designs",browseNote:"Browse the collection and enquire about a design.",searchPlaceholder:"Search by name or product ID",allCategories:"All categories",noProducts:"No designs match your search.",ourPromise:"OUR PROMISE",storyTitle:"Tradition that shines. Values that last.",storyText:"Explore our collection in person and contact our team for design availability and details.",whatsappUs:"Enquire on WhatsApp",comeVisit:"COME VISIT US",findShop:"Find our shop",contactDetails:"Contact details",callShop:"Call shop",visitInPerson:"Visit us in person",mapPlaceholder:"Add the shop's Google Maps link in the website settings.",getDirections:"Get directions →",available:"Available",unavailable:"Currently unavailable",weight:"Weight",enquire:"Enquire",noImage:"Photo unavailable",demoRates:"Demo rates — confirm with shop",searchAll:"All categories"},
  kn:{collections:"ಸಂಗ್ರಹಗಳು",rates:"ಲೋಹದ ದರಗಳು",visit:"ನಮ್ಮನ್ನು ಭೇಟಿ ಮಾಡಿ",eyebrow:"ಸಂಪ್ರದಾಯ · ನಂಬಿಕೆ · ಕಾಲಾತೀತ ಸೌಂದರ್ಯ",heroText:"ಜೀವನದ ವಿಶೇಷ ಕ್ಷಣಗಳಿಗೆ ಸುಂದರ ವಿನ್ಯಾಸಗಳನ್ನು ಅನ್ವೇಷಿಸಿ.",explore:"ಸಂಗ್ರಹಗಳನ್ನು ವೀಕ್ಷಿಸಿ",dailyRates:"ದೈನಂದಿನ ದರಗಳು",metalRates:"ಇಂದಿನ ಲೋಹದ ದರಗಳು",gold22:"22 ಕ್ಯಾರೆಟ್ ಚಿನ್ನ",gold24:"24 ಕ್ಯಾರೆಟ್ ಚಿನ್ನ",silver:"ಬೆಳ್ಳಿ",perGram:"ಪ್ರತಿ ಗ್ರಾಂ",rateNote:"ದರಗಳನ್ನು ಅಂಗಡಿಯವರು ನಮೂದಿಸುತ್ತಾರೆ. ಭೇಟಿ ನೀಡುವ ಮೊದಲು ಖಚಿತಪಡಿಸಿಕೊಳ್ಳಿ.",curated:"ನಿಮಗಾಗಿ ಆಯ್ಕೆ ಮಾಡಲಾಗಿದೆ",ourDesigns:"ನಮ್ಮ ವಿನ್ಯಾಸಗಳನ್ನು ವೀಕ್ಷಿಸಿ",browseNote:"ವಿನ್ಯಾಸಗಳನ್ನು ನೋಡಿ ಮತ್ತು ವಿಚಾರಿಸಿ.",searchPlaceholder:"ಹೆಸರು ಅಥವಾ ಉತ್ಪನ್ನ ಐಡಿ ಮೂಲಕ ಹುಡುಕಿ",allCategories:"ಎಲ್ಲಾ ವಿಭಾಗಗಳು",noProducts:"ನಿಮ್ಮ ಹುಡುಕಾಟಕ್ಕೆ ವಿನ್ಯಾಸಗಳು ಸಿಗಲಿಲ್ಲ.",ourPromise:"ನಮ್ಮ ಭರವಸೆ",storyTitle:"ಹೊಳೆಯುವ ಸಂಪ್ರದಾಯ. ಶಾಶ್ವತ ಮೌಲ್ಯಗಳು.",storyText:"ವಿನ್ಯಾಸಗಳ ಲಭ್ಯತೆ ಮತ್ತು ವಿವರಗಳಿಗಾಗಿ ನಮ್ಮನ್ನು ಸಂಪರ್ಕಿಸಿ.",whatsappUs:"WhatsApp ಮೂಲಕ ವಿಚಾರಿಸಿ",comeVisit:"ನಮ್ಮನ್ನು ಭೇಟಿ ಮಾಡಿ",findShop:"ನಮ್ಮ ಅಂಗಡಿಯನ್ನು ಹುಡುಕಿ",contactDetails:"ಸಂಪರ್ಕ ವಿವರಗಳು",callShop:"ಅಂಗಡಿಗೆ ಕರೆ ಮಾಡಿ",visitInPerson:"ಅಂಗಡಿಗೆ ಭೇಟಿ ನೀಡಿ",mapPlaceholder:"ವೆಬ್‌ಸೈಟ್ ಸೆಟ್ಟಿಂಗ್‌ಗಳಲ್ಲಿ Google Maps ಲಿಂಕ್ ಸೇರಿಸಿ.",getDirections:"ದಾರಿ ತಿಳಿಯಿರಿ →",available:"ಲಭ್ಯವಿದೆ",unavailable:"ಪ್ರಸ್ತುತ ಲಭ್ಯವಿಲ್ಲ",weight:"ತೂಕ",enquire:"ವಿಚಾರಿಸಿ",noImage:"ಚಿತ್ರ ಲಭ್ಯವಿಲ್ಲ",demoRates:"ಮಾದರಿ ದರಗಳು — ಅಂಗಡಿಯಲ್ಲಿ ಖಚಿತಪಡಿಸಿಕೊಳ್ಳಿ",searchAll:"ಎಲ್ಲಾ ವಿಭಾಗಗಳು"}
};

let language = localStorage.getItem("ashok-language") || "en";
let activeCategory = "all";
let bannerIndex = 0;
let bannerTimer = null;
const $ = (id) => document.getElementById(id);

function safeText(value){return String(value ?? "").replace(/[&<>"']/g, c=>({"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#39;"}[c]));}
function t(key){return I18N[language][key] || I18N.en[key] || key;}
function waUrl(message="Hello Ashok Jewellers, I would like to enquire about your jewellery designs.") {
  const digits = SHOP.whatsapp.replace(/\D/g,"");
  return `https://wa.me/${digits}?text=${encodeURIComponent(message)}`;
}
function setLanguage(next){
  language=next; localStorage.setItem("ashok-language",language);
  document.documentElement.lang=language==="kn"?"kn":"en";
  $("langToggle").textContent=language==="en"?"ಕನ್ನಡ":"English";
  document.querySelectorAll("[data-i18n]").forEach(el=>{const key=el.dataset.i18n;if(t(key))el.textContent=t(key);});
  document.querySelectorAll("[data-i18n-placeholder]").forEach(el=>el.placeholder=t(el.dataset.i18nPlaceholder));
  renderBanner();renderCategories();renderProducts();renderRates();
}
function renderBanner(){
  const b=BANNERS[bannerIndex];
  $("heroTitle").textContent=b.title[language];$("heroText").textContent=b.text[language];
  document.querySelector(".hero .eyebrow").textContent=b.eyebrow[language];
  $("heroImage").src=b.image;$("heroImage").alt=b.title[language];
  $("heroDots").innerHTML=BANNERS.map((_,i)=>`<button class="hero-dot ${i===bannerIndex?"active":""}" aria-label="Banner ${i+1}" data-slide="${i}"></button>`).join("");
  $("heroDots").querySelectorAll("button").forEach(btn=>btn.addEventListener("click",()=>{bannerIndex=Number(btn.dataset.slide);renderBanner();restartBanner();}));
}
function restartBanner(){if(bannerTimer)clearInterval(bannerTimer);bannerTimer=setInterval(()=>{bannerIndex=(bannerIndex+1)%BANNERS.length;renderBanner();},6500);}
function renderRates(){
  $("rate22").textContent=SHOP.rates.gold22?`₹${Number(SHOP.rates.gold22).toLocaleString("en-IN")}`:"₹ —";
  $("rate24").textContent=SHOP.rates.gold24?`₹${Number(SHOP.rates.gold24).toLocaleString("en-IN")}`:"₹ —";
  $("rateSilver").textContent=SHOP.rates.silver?`₹${Number(SHOP.rates.silver).toLocaleString("en-IN")}`:"₹ —";
  $("ratesUpdated").textContent=SHOP.rates.updatedAt;
}
function categories(){return [...new Set(PRODUCTS.map(p=>p.category))];}
function renderCategories(){
  const cats=categories();
  $("categoryFilter").innerHTML=`<option value="all">${safeText(t("allCategories"))}</option>`+cats.map(c=>`<option value="${safeText(c)}">${safeText(c)}</option>`).join("");
  $("categoryFilter").value=activeCategory;
  $("categoryChips").innerHTML=[["all",t("allCategories")],...cats.map(c=>[c,c])].map(([value,label])=>`<button class="chip ${activeCategory===value?"active":""}" data-category="${safeText(value)}">${safeText(label)}</button>`).join("");
  $("categoryChips").querySelectorAll("button").forEach(btn=>btn.addEventListener("click",()=>{activeCategory=btn.dataset.category;$("categoryFilter").value=activeCategory;renderCategories();renderProducts();}));
}
function renderProducts(){
  const query=$("searchInput").value.trim().toLowerCase();
  const filtered=PRODUCTS.filter(p=>(activeCategory==="all"||p.category===activeCategory)&&
    [p.id,p.name.en,p.name.kn,p.category,p.metal,p.purity].some(v=>String(v).toLowerCase().includes(query)));
  $("productGrid").innerHTML=filtered.map(p=>{
    const name=p.name[language]||p.name.en,desc=p.description[language]||p.description.en;
    const msg=`Hello Ashok Jewellers, I am enquiring about ${p.id} — ${name}. Is it available?`;
    const enquire=p.available?`<a class="product-enquire" href="${waUrl(msg)}" target="_blank" rel="noopener">${safeText(t("enquire"))} · WhatsApp</a>`:`<span class="product-enquire disabled">${safeText(t("unavailable"))}</span>`;
    return `<article class="product-card"><div class="product-photo"><img src="${safeText(p.image)}" alt="${safeText(name)}" loading="lazy" onerror="this.style.display='none';this.parentElement.style.background='#f0e4d2'">${p.available?"":`<span class="unavailable-badge">${safeText(t("unavailable"))}</span>`}</div><div class="product-body"><span class="product-id">${safeText(p.id)}</span><h3>${safeText(name)}</h3><p class="product-meta">${safeText(p.metal)} · ${safeText(p.purity)}</p><p class="product-meta">${safeText(t("weight"))}: ${safeText(p.weight)}</p><p class="product-description">${safeText(desc)}</p>${enquire}</div></article>`;
  }).join("");
  $("emptyState").hidden=filtered.length>0;
}
function configureShop(){
  $("shopAddress").textContent=SHOP.address;$("shopHours").textContent=SHOP.hours;
  $("callLink").href=`tel:${SHOP.phone}`;$("whatsappLink").href=waUrl();$("storyWhatsApp").href=waUrl();
  if(SHOP.mapsUrl){$("mapLink").href=SHOP.mapsUrl;$("mapLink").target="_blank";}else{$("mapLink").href="#visit";$("mapLink").removeAttribute("target");}
}
$("langToggle").addEventListener("click",()=>setLanguage(language==="en"?"kn":"en"));
$("searchInput").addEventListener("input",renderProducts);
$("categoryFilter").addEventListener("change",e=>{activeCategory=e.target.value;renderCategories();renderProducts();});
$("menuToggle").addEventListener("click",()=>document.querySelector(".desktop-nav").classList.toggle("open"));
document.querySelectorAll(".desktop-nav a").forEach(a=>a.addEventListener("click",()=>document.querySelector(".desktop-nav").classList.remove("open")));
$("year").textContent=new Date().getFullYear();
configureShop();setLanguage(language);restartBanner();

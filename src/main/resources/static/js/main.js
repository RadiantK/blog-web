// new Swiper(선택자, 옵션)
new Swiper('.main-slide .swiper', {
  initialSlide: 1,
  direction: 'horizontal',
  slidesPerView: 1, // 한번에 보여줄 슬라이드 개수(기본값 1);
  loop: true,
  autoplay: {
    delay: 5000
  },
  // pagination: {
  //   el: '.main-slide .swiper-pagination', // 페이지 번호 요소 선택자
  //   clickable: true // 사용자의 페이지 번호 요소 제어 가능 여부
  // },
  navigation: { // 슬라이드 이전/다음 버튼 사용 여부
    prevEl: '.main-slide .swiper-prev',
    nextEl: '.main-slide .swiper-next'
  }
});
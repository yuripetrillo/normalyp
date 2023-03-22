export default function getCarInfo() {
  const user = JSON.parse(localStorage.getItem('user'));

  if (user && user.accessToken) {
    return { CarInfo: user };
  } else {
    return {};
  }
}

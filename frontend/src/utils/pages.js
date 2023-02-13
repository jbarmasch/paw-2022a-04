export const parseLink = (header) => {
    let re = /<([^\?]+\?[a-z]+=([\d]+))>;[\s]*rel="([a-z]+)"/g;
    let arrRes = [];
    let obj = {};
    while ((arrRes = re.exec(header)) !== null) {
      obj[arrRes[3]] = {
        url: arrRes[1],
        page: arrRes[2]
      };
    }
    return obj;
  }
package logic;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dao.ItemDao;
import dao.UserDao;

@Service //@Component + service 기능 : controller와 db의 중간역할
public class ShopService {
	@Autowired //내컨테이너안의 객체중 자료형이 ItemDao인 것을 주입 ->@service
	private ItemDao itemDao;
	@Autowired
	private UserDao userDao;

	public List<Item> getItemList() {
		return itemDao.list();
	}

	public void itemCreate(Item item, HttpServletRequest request) { //item의 내용을 등록
		//업로드된 이미지 파일이 존재할 때
		if(item.getPicture()!=null && !item.getPicture().isEmpty()) {
			//파일 업로드 : 업로드된 파일의 내용을 파일에 저장
			uploadFileCreate(item.getPicture(),request,"item/img/");
							// ↑ picture을 파일화, 경로지정
			item.setPictureUrl(item.getPicture().getOriginalFilename());
		}
		itemDao.insert(item);
	}
	//↑
	private void uploadFileCreate(MultipartFile picture, HttpServletRequest request, String path) {
		//picture : 업로드된 파일의 내용
		String orgFile = picture.getOriginalFilename();
		String uploadPath = request.getServletContext().getRealPath("/") + path; //파일을 만들어줌
		File fpath = new File(uploadPath);
		if(!fpath.exists()) fpath.mkdirs(); //해당 path가없으면 생성
		try {
			//picture에 있는 것 파일로 생성
			picture.transferTo(new File(uploadPath + orgFile));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public Item getItem(int id) {
		return itemDao.selectOne(id);
	}

	public void itemUpdate(Item item, HttpServletRequest request) {
		//파일업로드가 있으면 업로드, 없으면 db만 수정
		//수정된 이미지 존재
		if(item.getPicture()!=null && !item.getPicture().isEmpty()) {
			uploadFileCreate(item.getPicture(),request,"item/img/");
			item.setPictureUrl(item.getPicture().getOriginalFilename());
		}
		itemDao.update(item);
	}

	public void itemDelete(int id) {
		itemDao.delete(id);
	}

	public void userInsert(User user) {
		userDao.insert(user);
	}

	public User getUser(String userid) {
		return userDao.selectOne(userid);
	}
}

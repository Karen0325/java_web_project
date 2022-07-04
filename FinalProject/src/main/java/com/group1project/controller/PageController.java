package com.group1project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.group1project.model.bean.Account;
import com.group1project.model.bean.Article;
import com.group1project.model.bean.Guide;
import com.group1project.model.bean.Product;
import com.group1project.model.bean.ProductComment;
import com.group1project.model.service.AccountService;
import com.group1project.model.service.ArticleService;
import com.group1project.model.service.GuideService;
import com.group1project.model.service.ProductCommentService;
import com.group1project.model.service.ProductService;

@SessionAttributes("account")
@Controller
public class PageController {
	
	@Autowired
	private ProductService pService ;
	
	@Autowired
	private ArticleService aService;

	@Autowired

	private ProductCommentService pcService;


		
	
//	########################前台商品頁面########################
	
	
	//前台商品頁面含page方法
	@GetMapping("front/productPage")
	public ModelAndView viewAllProducts(ModelAndView mav, 
			@RequestParam(name="p", defaultValue="1") Integer pageNumber) {
		Page<Product> page = pService.findByPage(pageNumber);
		
		mav.getModel().put("page", page);
		mav.setViewName("front/JoTravelFront/frontProductPage");
		return mav;
		
	}
	@GetMapping("/front/productPage/detail")
	public String frontIndexPage(@RequestParam("id") Integer productId,Model model){
		
		Product product= pService.getProductById(productId);
		List<ProductComment> pdComment =  pcService.getAllProductCommentByProductId(productId);
	
		model.addAttribute("product", product);
		model.addAttribute("pdComment", pdComment);
		
		return "front/JoTravelFront/frontProductDetail";
	} 
	
	
//	########################後台商品頁面########################
	
	@GetMapping("/back")
	public String backIndexPage(){
		return "backIndex";
	}
	
	@GetMapping("back/addProduct")
	public String addProduct(Model model){

		Product newPd = new Product();

		model.addAttribute("newPd", newPd);

		return "addProduct";
	}
		
	
	@GetMapping("back/allProduct")
	@ResponseBody
	public ModelAndView searchProductByPage(ModelAndView mav, 
			@RequestParam(name="p", defaultValue="1") Integer pageNumber,@RequestParam(value="key",defaultValue="" ,required = false) String key ,Model m) {
		
		Pageable pgb = PageRequest.of(pageNumber - 1, 5 ,Sort.Direction.DESC,"productId");
		Page<Product> page = pService.searchProductByNameWithPage(key, pgb);
		
		mav.getModel().put("page", page);
		mav.getModel().put("key", key);
		mav.setViewName("findAllProduct2");
		
		
		return mav;
	
	}
	
//	###################Start 商品評論############################
	
	@GetMapping("back/ProductComment/add")
	public String addProductComment(@RequestParam(name="id") Integer productId,Model model) {
		
		
		model.addAttribute("pdComment", new ProductComment()); 
		model.addAttribute("pid", productId);
		return "addProductComment";
	}
	
	@GetMapping("back/ProductComment/all")
	public String findAllPrdouctComment(Model model) {
		
		return "findAllProductComment";
		
	}
	
	@GetMapping("back/ProductComment/search")
	public String findAllPrdouctComment(@RequestParam(value="id", defaultValue="" ,required = false) Integer prdouctId ,Model model) {
		
		
		List<ProductComment> searchPdC =  pcService.getAllProductCommentByProductId(prdouctId);
		
		for(ProductComment one:searchPdC) {
		System.out.println(one.getProuctCommentId());
		System.out.println(one.getAccount());
		System.out.println(one.getProductComment());

		}
		model.addAttribute("searchPdC", searchPdC);
		
		return "searchProductComment";
		
	}
	
	
//	@GetMapping("searchProduct")
//	@ResponseBody
//	public Page<Product> searchProduct(@RequestParam("key") String key,@RequestParam(name = "p",defaultValue = "1") Integer pageNumber,Model m) {
//	
//		 Pageable pgb = PageRequest.of(pageNumber - 1, 3 ,Sort.Direction.DESC,"productId");
//		 
//		 m.addAttribute("page", pgb);
//		 
//		 return pService.searchProductByNameWithPage(key, pgb);
//	
//	}

	
	
	// ##### Start ##### feedback Page Controller
	
	
	
	
	

	@GetMapping("article/add")
	public String addArticlePage(Model model) {
		
		Article article=new Article();
		
		
		
		model.addAttribute("article", article);
	
		
		return "editArticle";
	}
	

	@GetMapping("searchProduct")
	@ResponseBody
	public Page<Product> searchProduct(@RequestParam("key") String key,@RequestParam(name = "p",defaultValue = "1") Integer pageNumber,Model m) {
	
		 Pageable pgb = PageRequest.of(pageNumber - 1, 3 ,Sort.Direction.DESC,"productId");
		 
		 m.addAttribute("page", pgb);
		 
		 
		 return pService.searchProductByNameWithPage(key, pgb);
	
	}

	
	
	@GetMapping("article/all")
	public ModelAndView viewAllArticles(ModelAndView mav, 
			@RequestParam(name="p", defaultValue = "1") Integer pageNumber,@RequestParam(value="key",defaultValue="" ,required = false) String key,Model m) {
		//Page<Article> page = aService.findByPage(pageNumber);
		
		Pageable pgb = PageRequest.of(pageNumber - 1, 5 ,Sort.Direction.DESC,"articleId");

		Page<Article> page = aService.searchArticleByTitleWithPage(key, pgb);

		mav.getModel().put("page", page);
		mav.getModel().put("key", key);
		mav.setViewName("viewArticles");
		return mav;
	}

	
	
	@GetMapping("front666/")
	public String frontPage(){
		return "frontend";
	}
//	@GetMapping("blogIndex/")
//	public String blogIndexPage(){
//		return "blogIndex";
//	} 
	//前台商品頁面含page方法
		@GetMapping("front/blogIndex")
		public ModelAndView viewAllArticlePage(ModelAndView mav, 
				@RequestParam(name="p", defaultValue="1") Integer pageNumber,@RequestParam(value="key",defaultValue="" ,required = false) String key,Model m) {
//			Page<Article> page = aService.findByPage(pageNumber);
//			
//			mav.getModel().put("page", page);
//			mav.getModel().put("key", key);
			Pageable pgb = PageRequest.of(pageNumber - 1, 5 ,Sort.Direction.DESC,"articleId");

			Page<Article> page = aService.searchArticleByTitleWithPage(key, pgb);

			mav.getModel().put("page", page);
			mav.getModel().put("key", key);
			mav.setViewName("front/JoTravelFront/blogIndex");
			return mav;
		}
		
		@GetMapping("/front/blogPage/detail")
		public String frontBlogPage(@RequestParam("id") Integer articleId,Model model){
			
			Article article= aService.getArticleById(articleId);
//			List<ProductComment> pdComment =  pcService.getAllProductCommentByProductId(articleId);
		
			model.addAttribute("article", article);
//			model.addAttribute("pdComment", pdComment);
			
			return "front/JoTravelFront/blogSingle";
		} 
		
		@GetMapping("/front/addBlogPage")
		public String addFrontBlogPage(Model model){
			
			Article article=new Article();
			
			
			
			model.addAttribute("article", article);
			
			return "front/JoTravelFront/addBlog";
		} 
		
	}
		
//		@GetMapping("front/blogSingle")
//		public String blogSingle(){
//			return "front/JoTravelFront/blogSingle";
//		} 	
//	@GetMapping("blogPage/")
//	public String blogPage(){
//		return "blogPage";
//	} 
//}


package com.campfire.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.campfire.domain.Criteria;
import com.campfire.domain.PageDTO;
import com.campfire.domain.guideBoard.GuideBoardAttachVO;
import com.campfire.domain.guideBoard.GuideBoardVO;
import com.campfire.domain.tipBoard.TipBoardAttachVO;
import com.campfire.domain.tipBoard.TipBoardVO;
import com.campfire.service.GuideBoardService;
import com.campfire.service.TipBoardService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@AllArgsConstructor
@RequestMapping("/campfire/*")
public class CampfireController {
	private TipBoardService t_service;
	@GetMapping("/tipList")
	public void tipList(Criteria cri, Model model) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1); //7일간 보이도록 하기위해서.
		String nowday = format.format(cal.getTime());
		
		model.addAttribute("nowday",nowday);
		model.addAttribute("list", t_service.getList(cri));
		model.addAttribute("pageMaker", new PageDTO(cri, t_service.getTotal(cri)));
	}

	//상세보기로 이동
	@GetMapping({"/tipView", "/tipModify"})
	public void tip(@RequestParam("bno") Long bno, @ModelAttribute("cri") Criteria cri, Model model) {
		model.addAttribute("board", t_service.get(bno));
	}
	@GetMapping("/tipWrite")
	public void tipWrite() {;}
	@PostMapping("/tipWrite")
	public String tipWrite(TipBoardVO t_vo, RedirectAttributes rttr) {
		System.out.println(t_vo.getTitle());
		if(t_vo.getAttachList() != null) {
			t_vo.getAttachList().forEach(log::info);
		}
		t_service.register(t_vo);
		rttr.addFlashAttribute("result", t_vo.getBno());
		return "redirect:/campfire/tipList";
	}
	
	//게시글 수정
	@PostMapping("/tipModify")
	public String modify(TipBoardVO t_vo, Criteria cri, RedirectAttributes rttr) {
		if(t_service.modify(t_vo)) {
			rttr.addFlashAttribute("result", "success");
		}
		rttr.addAttribute("pageNum", cri.getPageNum());
		rttr.addAttribute("amount", cri.getAmount());
		rttr.addAttribute("type", cri.getType());
		rttr.addAttribute("keyword", cri.getKeyword());
		
		return "redirect:/campfire/tipList";
	}
	
	//게시글 및 첨부파일 삭제
	@GetMapping("/tipRemove")
	public String remove(@RequestParam("bno") Long bno, Criteria cri, RedirectAttributes rttr) {
		List<TipBoardAttachVO> attachList = t_service.getAttachList(bno);
		if(t_service.remove(bno)) {
			deleteFiles(attachList);
			rttr.addFlashAttribute("result", "success");
		}
		rttr.addAttribute("pageNum", cri.getPageNum());
		rttr.addAttribute("amount", cri.getAmount());
		rttr.addAttribute("type", cri.getType());
		rttr.addAttribute("keyword", cri.getKeyword());
		
		return "redirect:/campfire/tipList";
	}
	
	//게시글에 등록된 모든 첨부파일 목록
	@ResponseBody
	@GetMapping(value="/getAttachList", produces= {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<List<TipBoardAttachVO>> getAttachList(Long bno){
		return new ResponseEntity<List<TipBoardAttachVO>>(t_service.getAttachList(bno), HttpStatus.OK);
	}
	
	//첨부파일 및 썸네일파일 삭제 메소드
	private void deleteFiles(List<TipBoardAttachVO> attachList) {
		if(attachList == null || attachList.size() == 0) {return;}
		
		attachList.forEach(g_vo -> {
			try {
				Path origin = Paths.get("C:\\upload\\" + g_vo.getUploadPath() + "\\" + g_vo.getUuid() + "_" + g_vo.getFileName());
				Files.delete(origin);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
}

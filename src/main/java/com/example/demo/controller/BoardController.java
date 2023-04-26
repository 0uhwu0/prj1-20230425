package com.example.demo.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.*;

import com.example.demo.domain.*;
import com.example.demo.service.*;

@Controller
@RequestMapping("/")
public class BoardController {

	@Autowired
	private BoardService service;
	
	// 경로 : http://localhost:8080
	// 경로 : http://localhost:8080/list
	// 게시물 목록
	// @RequestMapping(value = {"/", "list"}, method = RequestMethod.GET)

	// 테이블 형식으로 전체 게시물 보는 역할
	@GetMapping({"/", "list"})
	public String list(Model model) {
		// 1. request param 수집 / 가공
		// 2. business logic 처리
		List<Board> list = service.listBoard();
		// 3. add attribute
		model.addAttribute("boardList", list);
		
		// 4. forward / redirect
		return "list";
	}
	
	// 원하는 id의 게시물 보기
	@GetMapping("/id/{id}")
	public String board(@PathVariable("id") Integer id, Model model) {
		// 1. request param
		// 2. business logic
		Board board = service.getBoard(id);
		// 3. add attribute
		model.addAttribute("board", board);
		// 4. forward / redirect
		return "get";
	}
	
	// 수정하기 전 게시물 보기
	@GetMapping("/modify/{id}")
	public String modifyForm(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("board", service.getBoard(id));
		return "modify";
	}
	
	// 수정하기
	// @RequestMapping(value = "/modify/{id}", method = RequestMethod.POST)
	@PostMapping("/modify/{id}")
	public String modifyProcess(Board board, RedirectAttributes rttr) {
		
		boolean ok = service.modify(board);
		
		if (ok) {
			// 해당 게시물 보기로 리디렉션
			rttr.addAttribute("success", "success");
			return "redirect:/id/" + board.getId(); 
		} else {
			// 수정 form 으로 리디렉션
			rttr.addAttribute("fail", "fail");
			return "redirect:/modify/" + board.getId();
		}
	}
	
	// 삭제하기
	@PostMapping("remove")
	public String remove(Integer id, RedirectAttributes rttr) {
		boolean ok = service.remove(id);
		if (ok) {
			rttr.addAttribute("success", "remove");
			return "redirect:/list";
		} else {
			return "redirect:/id" + id;
		}
	}
}
package com.efficientsciences.cowsandbulls.wordwars.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;

public class Pages {

	public List<Page> pages=new ArrayList<Page>();

	public List<Page> getPages() {
		return pages;
	}

	public void setPages(List<Page> pages) {
		this.pages = pages;
	}

	public List<Page> addPage(Page page){
		int pageIndex = 0;
		if(null!=this.pages && !this.pages.isEmpty()){
			pageIndex= this.pages.size()+1;
		}
		page.setPageIndex(pageIndex);
		this.pages.add(page);

		return this.pages;
	}

	public Page getPageByIndex(int pageIndex){
		if(null!=this.pages && this.pages.size()> pageIndex){
			return this.pages.get(pageIndex);
		}
		else{
			return null;
		}
	}

	public boolean addPageChunk(PageChunk p,int pageIndex) {
		Page currentpage=getPageByIndex(pageIndex);
		if(hasPagechunk(p)){
			return false;
		}
		else if(null!=currentpage && currentpage.getChunks().size()<ResourcesManager.getInstance().maxPageChunksInPage){

			return currentpage.addPageChunks(p);
		}
		else{
			Page page = new Page();
			page.addPageChunks(p);
			addPage(page);
			ResourcesManager.getInstance().currentPageIndex=page.getPageIndex();
			return true;
		}
	}

	public boolean hasPagechunk(PageChunk p) {
		for (Iterator<Page> iterator = pages.iterator(); iterator
				.hasNext();) {
			Page page = (Page) iterator.next();
			if(page.hasPageChunk(p)){
				return true;
			}
		}
		return false;
	}

	/**
	 * @return
	 */
	public List<PageChunk> getUsablePageChunksTotal() {
		List <PageChunk> usableChunks= new ArrayList<PageChunk>();
		for (Iterator<Page> iterator = pages.iterator(); iterator
				.hasNext();) {
			Page page = (Page) iterator.next();
			if(null!=page && null!=page.getChunks()){
				for (PageChunk pageChunk : page.getChunks()) {
					if(usableChunks.size()<5){
						usableChunks.add(pageChunk);
					}
				}
			}
		}
		return usableChunks;
	}

	/**
	 * @return
	 */
	public List<PageChunk> getUsablePageChunksBull() {
		List <PageChunk> usableChunks= new ArrayList<PageChunk>();
		for (Iterator<Page> iterator = pages.iterator(); iterator
				.hasNext();) {
			Page page = (Page) iterator.next();
			if(null!=page && null!=page.getChunks()){
				for (PageChunk pageChunk : page.getChunks()) {
					if(pageChunk.getBulls()>1 && pageChunk.getBulls()>(ResourcesManager.getInstance().numberOfLettersHosted/2-1) && pageChunk.getBulls()<ResourcesManager.getInstance().numberOfLettersHosted-1 && usableChunks.size()<2){
						usableChunks.add(pageChunk);
					}
				}
			}
		}
		return usableChunks;
	}

}

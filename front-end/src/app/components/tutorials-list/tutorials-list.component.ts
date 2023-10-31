import {Component, OnInit} from '@angular/core';
import {Tutorial} from "../../models/tutorial.model";
import {TutorialService} from "../../services/tutorial.service";

@Component({
  selector: 'app-tutorials-list',
  templateUrl: './tutorials-list.component.html',
  styleUrls: ['./tutorials-list.component.css']
})
export class TutorialsListComponent implements OnInit{

  tutorials: Tutorial[] = [];
  currentTutorial: Tutorial = {};
  currentIndex: number = -1;
  title: string = '';

  page: number = 1;
  count: number = 0;
  pageSize: number = 3;
  pageSizes: number[] = [3, 6, 9];
  constructor(private tutorialService: TutorialService) { }



  ngOnInit(): void {
    this.retrieveTutorials();
  }

  getRequestParams(searchTitle: string, page: number, pageSize: number): any{
    let params: any = {};

    if(searchTitle){
      params.title = searchTitle;
    }
    if(page){
      params.page = page-1;
    }
    if(pageSize){
      params.size = pageSize;
    }
    return params;
  }

  retrieveTutorials(): void{
    const params = this.getRequestParams(this.title, this.page, this.pageSize);

    this.tutorialService.getAll(params).subscribe({
      next: (data) => {
        const {tutorials, totalItems} =  data;
        this.tutorials = tutorials;
        this.count = totalItems;
        console.log(data);
      },
      error: (err) => console.error(err)
    });
  }

  onPageChange(event: number): void{
    this.page = event;
    this.retrieveTutorials();
  }

  onPageSizeChange(event: any): void{
    this.pageSize = event.target.value;
    this.page = 1;
    this.retrieveTutorials();
  }

  refreshList(): void{
    this.retrieveTutorials();
    this.currentTutorial = {};
    this.currentIndex = -1;
  }

  setActiveTutorial(tutorial: Tutorial, idx: number){
    this.currentTutorial = tutorial;
    this.currentIndex = idx;
  }

  removeAllTutorials(): void{
    this.tutorialService.deleteAll().subscribe({
      next: (response) => {
        console.log(response);
        this.refreshList();
      },
      error: (err) => console.error(err)
    });
  }

  searchTitle():void {
    this.page = 1;
    this.retrieveTutorials();
  }

}

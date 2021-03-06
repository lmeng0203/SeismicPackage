/*<license>
 -------------------------------------------------------------------------------
  Copyright (c) 2007 ConocoPhillips Company
 
  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:
 
  The above copyright notice and this permission notice shall be included in all
  copies or substantial portions of the Software.
 
  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  SOFTWARE.
 -------------------------------------------------------------------------------
 </license>*/
#include "sd_jacket.h"
#include "ipc/sd_master.hh"

#ifndef XM_H
#include <Xm/Xm.h>
#define XM_H
#endif

void sd_new(char *node, Widget w, int colors, int planes, void **sd,
	char *title)
{
	*sd = (void *) new SlaveDisplayMaster(node, w, colors, planes,
		(SlaveDisplayMaster **) sd, title);
}

void sd_delete(void *sd)
{
	delete (SlaveDisplayMaster *) sd;
}

void sd_addPixmap(void *sd, Pixmap pixmap)
{
	((SlaveDisplayMaster *) sd)->addPixmap(pixmap);
}

void sd_deletePixmap(void *sd, Pixmap pixmap)
{
	((SlaveDisplayMaster *) sd)->deletePixmap(pixmap);
}

void sd_displayPixmap(void *sd, Pixmap pixmap)
{
	((SlaveDisplayMaster *) sd)->displayPixmap(pixmap);
}

void sd_updateRGBs(void *sd)
{
	((SlaveDisplayMaster *) sd)->updateRGBs();
}

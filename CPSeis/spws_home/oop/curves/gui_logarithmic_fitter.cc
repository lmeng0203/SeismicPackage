#include "curves/gui_logarithmic_fitter.hh"
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
#include "curves/curve_fitter.hh"
#include "curves/ls_logarithmic_fitter.hh"
#include "sl/sl_scale_text_arrow.hh"
#include "sl/psuedo_widget.hh"
#include <Xm/Label.h>
#include <math.h>
#include <float.h>

GuiLogarithmicFitter::GuiLogarithmicFitter (Widget parent, char *name,
  HelpCtx hctx, const char *title) :
  GuiLinearFitter (parent, name, hctx, title, CV::LOGARITHMIC)
{
}

GuiLogarithmicFitter::GuiLogarithmicFitter (SLDelay *container, char *name,
  HelpCtx hctx, const char *title) :
  GuiLinearFitter (container, name, hctx, title, CV::LOGARITHMIC)
{
}

Widget GuiLogarithmicFitter::make (Widget parent)
{
  if (made()) return topWidget ();

  parent = parent ? parent : wParent ();

  GuiCurveFitter::make (parent); // grandparent

  Widget exponent, Iequals;

  exponent = XtVaCreateManagedWidget ("exponent",
                         xmLabelWidgetClass,  topWidget(),
                         XmNborderWidth,      0,
                         NULL);

  Iequals = XtVaCreateManagedWidget ("Iequals",
                         xmLabelWidgetClass,  topWidget(),
                         XmNborderWidth,      0,
                         NULL);

// attach the managed widgets as desired
  XtVaSetValues (exponent,
                         XmNtopAttachment,    XmATTACH_WIDGET,
                         XmNtopWidget,        _top_attach_widget,
                         XmNtopOffset,        30,
                         XmNleftAttachment,   XmATTACH_FORM,
                         NULL);

  XtVaSetValues (_dependtxt[2],
                         XmNtopAttachment,    XmATTACH_OPPOSITE_WIDGET,
                         XmNtopWidget,        exponent,
                         XmNtopOffset,        -7,
                         XmNleftAttachment,   XmATTACH_WIDGET,
                         XmNleftWidget,       exponent,
                         NULL);

  XtVaSetValues (Iequals,
                         XmNtopAttachment,    XmATTACH_OPPOSITE_WIDGET,
                         XmNtopWidget,        exponent,
                         XmNleftAttachment,   XmATTACH_WIDGET,
                         XmNleftWidget,       _dependtxt[2],
                         NULL);

  XtVaSetValues (_a0_arrow->W(),
                         XmNtopAttachment,    XmATTACH_OPPOSITE_WIDGET,
                         XmNtopWidget,        exponent,
                         XmNleftAttachment,   XmATTACH_WIDGET,
                         XmNleftWidget,       Iequals,
                         NULL);

  XtVaSetValues (_indeptxt[2],
                         XmNtopAttachment,    XmATTACH_OPPOSITE_WIDGET,
                         XmNtopWidget,        exponent,
                         XmNleftAttachment,   XmATTACH_WIDGET,
                         XmNleftWidget,       _a0_arrow->W(),
                         NULL);

  XtVaSetValues (_a1_arrow->W(),
                         XmNtopAttachment,    XmATTACH_OPPOSITE_WIDGET,
                         XmNtopWidget,        exponent,
                         XmNtopOffset,        -20,
                         XmNleftAttachment,   XmATTACH_WIDGET,
                         XmNleftWidget,       _indeptxt[2],
                         NULL);
  if (displayErrorStatistics()) {
    XtVaSetValues (_erraveW,
                         XmNtopAttachment,    XmATTACH_OPPOSITE_WIDGET,
                         XmNtopWidget,        exponent,
                         XmNleftAttachment,   XmATTACH_WIDGET,
                         XmNleftWidget,       _a1_arrow->W(),
                         NULL);

    XtVaSetValues (_erravetxt,
                         XmNtopAttachment,    XmATTACH_OPPOSITE_WIDGET,
                         XmNtopWidget,        exponent,
                         XmNleftAttachment,   XmATTACH_WIDGET,
                         XmNleftWidget,       _erraveW,
                         NULL);

    XtVaSetValues (_errstdW,
                         XmNtopAttachment,    XmATTACH_OPPOSITE_WIDGET,
                         XmNtopWidget,        exponent,
                         XmNleftAttachment,   XmATTACH_WIDGET,
                         XmNleftWidget,       _erravetxt,
                         NULL);

    XtVaSetValues (_errstdtxt,
                         XmNtopAttachment,    XmATTACH_OPPOSITE_WIDGET,
                         XmNtopWidget,        exponent,
                         XmNleftAttachment,   XmATTACH_WIDGET,
                         XmNleftWidget,       _errstdW,
                         NULL);
  }

  dataChangedFit ();

  return topWidget ();
}
